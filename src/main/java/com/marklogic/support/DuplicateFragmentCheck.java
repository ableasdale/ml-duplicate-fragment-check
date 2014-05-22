package com.marklogic.support;

import com.marklogic.xcc.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.util.*;

/**
 * Checks a given MarkLogic database (and associated forests) for duplicate fragments in multiple forests
 *
 * User: Alex
 * Date: 22/05/14
 * Time: 10:51
 */
public class DuplicateFragmentCheck {

    private static final String DATABASE_NAME = "YourDBNameHere";
    private static final String CONNECTION_URI = "xcc://q:q@192.168.1.104:9999/"+DATABASE_NAME;

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
        LOG.info("The database '" + DATABASE_NAME + "' contains " +  rs.size() + " forests.");
        s.close();

        // arraylist to place all forest URI lists in case the information is needed later
        Map<String, List<String>> uriMap = new HashMap<String, List<String>>();
        // set to place all distinct URIs for fast duplicate checks
        Set set = new HashSet();
        // duplicate URI list for later reference
        List<String> dupes = new ArrayList<String>();

        for (int i = 1; i <= rs.size(); i++){
            ResultItem ri = rs.resultItemAt(i - 1);
            String forestId = ri.getItem().toString();
            LOG.info("Getting list of URIs for forest ( "+i+" of "+rs.size()+" ): " + forestId);
            List<String> items = new ArrayList<String>();
            Session s2 = cs.newSession();

            Request r2 = s2.newAdhocQuery("declare variable $fid as xs:string external;\ncts:uris((), (), (), (), $fid cast as xs:unsignedLong)");
            r2.setNewStringVariable("fid", forestId);
            ResultSequence rs2 = s2.submitRequest (r2);
            for (ResultItem ri2 : rs2.toResultItemArray()){
                String currentItem = ri2.getItem().toString();
                items.add(currentItem);
                if (set.contains(currentItem)){
                    LOG.info("Duplicate URI detected: " + currentItem + " in forest: " + forestId);
                    dupes.add(currentItem);
                } else {
                    set.add(currentItem);
                }
            }
            uriMap.put(ri.getItem().toString(), items);
            LOG.info(items.size() + " URIs found in forest: " + forestId + " at " + new Date());
            s2.close();
        }
        LOG.info("URI / Forest mapping complete for all forests: " + set.size() + " unique URIs " + dupes.size() + " duplicate URIs found");


        LOG.info("DEBUG POINT");

    }
}
