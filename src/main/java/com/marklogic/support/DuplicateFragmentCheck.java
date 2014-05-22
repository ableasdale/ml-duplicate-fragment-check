package com.marklogic.support;

import com.marklogic.xcc.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.net.URI;
import java.util.*;

/**
 * Checks a given MarkLogic database (and associated forests) for duplicate fragments in multiple forests
 *
 * User: Alex Bleasdale
 * Date: 22/05/14
 * Time: 10:51
 */
public class DuplicateFragmentCheck {

    private static final String DATABASE_NAME = "mydb";
    private static final String CONNECTION_URI = "xcc://q:q@192.168.1.104:9999/"+DATABASE_NAME;
    public static final String CSV_FILENAME = "e:\\duplicate-report.csv";

    private static final Logger LOG = LoggerFactory
            .getLogger(DuplicateFragmentCheck.class);


    public static void main(String[] args) throws Exception {
        LOG.info("Application started at: " + new Date());

        ContentSource cs =
                ContentSourceFactory.newContentSource(new URI(CONNECTION_URI));

        // Initial forest count
        Session s = cs.newSession();
        Request r = s.newAdhocQuery("declare variable $db as xs:string external;\nxdmp:database-forests(xdmp:database($db))");
        r.setNewStringVariable("db", DATABASE_NAME);
        ResultSequence rs = s.submitRequest (r);
        LOG.info(String.format("The database '%s' contains %d forests.", DATABASE_NAME, rs.size()));
        s.close();

        // forest-id keyed map to place all full forest URI lists in case the information is needed later
        Map<String, Set> masterUriSets = new HashMap<String, Set>();
        // distinctUris to place all distinct URIs for fast duplicate checks
        Set<String> distinctUris = new HashSet<String>();
        // duplicate URI list for later reference
        Set<String> dupes = new HashSet<String>();

        for (int i = 1; i <= rs.size(); i++){
            ResultItem ri = rs.resultItemAt(i - 1);
            String forestId = ri.getItem().toString();
            LOG.info(String.format("Getting full list of URIs for forest ( %d of %d ): %s", i, rs.size(), forestId));
            Set items = new HashSet<String>();
            Session s2 = cs.newSession();

            Request r2 = s2.newAdhocQuery("declare variable $fid as xs:string external;\ncts:uris((), (), (), (), $fid cast as xs:unsignedLong)");
            r2.setNewStringVariable("fid", forestId);
            ResultSequence rs2 = s2.submitRequest (r2);
            LOG.info("Complete URI list obtained from the server at: "+ new Date());
            s2.close();
            LOG.info("Running duplicate URI check...");
            for (ResultItem ri2 : rs2.toResultItemArray()){
                String currentItem = ri2.getItem().toString();
                items.add(currentItem);
                if (distinctUris.contains(currentItem)){
                    LOG.debug(String.format("Duplicate URI detected: %s in forest: %s", currentItem, forestId));
                    dupes.add(currentItem);
                } else {
                    distinctUris.add(currentItem);
                }
            }
            masterUriSets.put(forestId, items);
            LOG.info(String.format("%d URIs found in forest-id: %s at %s", items.size(), forestId, new Date()));
        }
        LOG.info(String.format("URI / Forest mapping complete for all forests: %d unique URIs %d duplicate URIs found", distinctUris.size(), dupes.size()));
        LOG.info("Generating Full CSV report...");
        StringBuilder sb = new StringBuilder();
        for (String item : dupes){
            sb.append(item).append(",");
            for (String forest : masterUriSets.keySet()) {
                //LOG.info(forest + " | "+ item);
                Set x = masterUriSets.get(forest);
                if (x.contains(item)){
                    sb.append(forest).append(",");
                }
            }
            sb.append("\n");
            //LOG.info(String.format("Adding: %s", item));
        }
        // create file
        File file = new File(CSV_FILENAME);
        BufferedWriter writer = new BufferedWriter(new FileWriter(file));
        try {
            writer.write(sb.toString());
        } finally {
            if (writer != null) writer.close();
        }
        LOG.info(String.format("Full CSV report completed - file saved as %s", CSV_FILENAME));

    }
}
