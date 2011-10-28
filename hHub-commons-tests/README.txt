This module provides tests for the hHub-commons module.  This has been separated 
out because the tests depend on the Sprig Web Test library, which provides a nice mock 
server.  However, the android maven process will not exclude this library and
its dependencies (which are not Android compatible) from the apk lib.