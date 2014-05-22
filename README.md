ml-duplicate-fragment-check
===========================

Tool for identifying duplicate URIs in multiple forests in a given MarkLogic Database.  This will generate a CSV file containing all duplicate URIs and the forest ids containing them.

###To use###
1. This has Maven dependencies (logback and xcc/j)
2. Execute **com.marklogic.support.DuplicateFragmentCheck**.main() 

### Configuration ###

Set up the application by configuring the following values in DuplicateFragmentCheck.java:

#### XCC Connection URI ####

    private static final String USER = "q";
    private static final String PASSWORD = "q";
    private static final String HOST = "192.168.1.104";
    private static final String PORT = "9999";
    private static final String DATABASE_NAME = "mydb";

#### CSV Output File ####

	public static final String CSV_FILENAME = "e:\\duplicate-report.csv";

###Console Output###

    
	14:07:26.187 [main] INFO  c.m.support.DuplicateFragmentCheck - Application started at: Thu May 22 14:07:26 BST 2014
	14:07:26.310 [main] INFO  c.m.support.DuplicateFragmentCheck - The database 'mydb' contains 3 forests.
	14:07:26.311 [main] INFO  c.m.support.DuplicateFragmentCheck - Getting full list of URIs for forest ( 1 of 3 ): 17685282256492605229
	14:07:46.491 [main] INFO  c.m.support.DuplicateFragmentCheck - Complete URI list obtained from the server at: Thu May 22 14:07:46 BST 2014
	14:07:46.491 [main] INFO  c.m.support.DuplicateFragmentCheck - Processing forest URIs ...
	14:07:48.821 [main] INFO  c.m.support.DuplicateFragmentCheck - 3167400 URIs found in forest-id: 17685282256492605229 at Thu May 22 14:07:48 BST 2014
	14:07:48.821 [main] INFO  c.m.support.DuplicateFragmentCheck - Getting full list of URIs for forest ( 2 of 3 ): 17994883593507302826
	14:08:29.466 [main] INFO  c.m.support.DuplicateFragmentCheck - Complete URI list obtained from the server at: Thu May 22 14:08:29 BST 2014
	14:08:29.469 [main] INFO  c.m.support.DuplicateFragmentCheck - Processing forest URIs ...
	14:08:31.826 [main] INFO  c.m.support.DuplicateFragmentCheck - 3165178 URIs found in forest-id: 17994883593507302826 at Thu May 22 14:08:31 BST 2014
	14:08:33.100 [main] INFO  c.m.support.DuplicateFragmentCheck - Getting full list of URIs for forest ( 3 of 3 ): 10607798474835080749
	14:09:34.469 [main] INFO  c.m.support.DuplicateFragmentCheck - Complete URI list obtained from the server at: Thu May 22 14:09:34 BST 2014
	14:09:34.511 [main] INFO  c.m.support.DuplicateFragmentCheck - Processing forest URIs ...
	14:11:21.253 [main] INFO  c.m.support.DuplicateFragmentCheck - 3189005 URIs found in forest-id: 10607798474835080749 at Thu May 22 14:11:21 BST 2014
	14:11:21.269 [main] INFO  c.m.support.DuplicateFragmentCheck - URI / Forest mapping complete for all forests: 9478572 unique URIs 43011 duplicate URIs found
	14:11:21.270 [main] INFO  c.m.support.DuplicateFragmentCheck - Generating Full CSV report...
	14:11:29.557 [main] INFO  c.m.support.DuplicateFragmentCheck - Full CSV report completed - file saved as e:\duplicate-report.csv
