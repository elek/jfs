
Projects: 
* gdocs - javai.nio.filesystem.FileSystem implmenetation to save and read files to/from google docs/drive
* cli - simple test shell to test any file system provider
* sync - cli interface for a simple FileVisitor which backup files to/from a remote file system

Current status: draft

I am using it to bulk upload and download from/to the google drive but a lot of things still missing:

1. the nio FileSystem API is not fully implemented (move,copy,delete is missing)
2. no settings for adjusting cache settings
3. sync cli supports only one way sync without overwriting any file
4. syntax of the sync app will be changed
5. Atribute handling should be improved


Usage:

`java -jar cli.jar file:/// /tmp/todir gdocs:/// /archive`

or from java code:

```java
Path p = FileSystems.getFileSystem(new URI("gdocs://docs.google.com")).getPath("/archiv/auto/something.pdf");

Path to = FileSystems.getDefault().getPath("/tmp").resolve(p.getRoot().relativize(p).toString());

Files.createDirectories(to.getParent());
````
