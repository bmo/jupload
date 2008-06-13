
0.0	INFORMATION
1.0	DESCRIPTION
2.0 HOWTO USE IT
3.0 Self Sign Applet.
4.0	Server scripts
4.0	FAQ:



~~~~~~~~~~~~~~~~~~~
0.0	INFORMATION
~~~~~~~~~~~~~~~~~~~


WEB SITE: http://jupload.sourceforge.net/
Version : 2.6.0

This applet comes with source code. I take no responsibility for any damages caused by the usage of this applet. 
Use it at your own risk!

This applet is 'given' with the GPL licence.

~~~~~~~~~~~~~~~~~~~
1.0	DESCRIPTION
~~~~~~~~~~~~~~~~~~~
Traditional HTML upload forms allow you to select and upload one file at a time. This restriction is unacceptable 
when it comes to uploading thousands of files within a single folder. 

Java Multiple File Upload Applet (JUpload) takes care of this limited by allowing users to select and upload a whole 
directory and the files within it with a single click.

Main evolution since V2 :
- Add of UploadPolicy. This allows developpers to easily configure the way files are uploaded (see below for details)
- Add of picture management functions. This can easily be done by using the PictureUploadPolicy, or one of its 
inherited classes, like CoppermineUploadPolicy (which is dedicated to the coppermine online picture gallery.
- Cookies session are kept: Upload are now done within the current navigator session. Thus, the upload is done within 
the same user session, if any.
- Works with SSL.
- Works with FTP (since 2.9.0).
- Upload is done in the current HTTP Session (the applet reads the cookies from the navigator).

UploadPolicies makes it easy to configure these parameters:
- Target upload URL
- Number of files that should be uploaded. For instance: all at once (default), one by one (see FileByFileUploadPolicy), 
or by packet of limited number of files (see CustomizedNbFilesPerRequestUploadPolicy)
- Top part of the applet can be modified by writing a new UploadPolicy, using the UploadPolicy.createTopPanel method. 
The PictureUploadPolicy uses it to add a preview panel and two rotation buttons.
- And much more ...  Please see the javadoc and the "HowTo customize it" page.

 Picture management is added by the PictureUploadPolicy. (see the Demo of the picture applet). This includes the 
 following parameters:
- Ability to set a maximum width and/or height for pictures,
- Ability to rotate pictures, by quarters of turn,
- Ability to preview pictures. A click on the small picture displays a full screen picture.

Translation.
  The JUpload package contains the following languages:
- Bresilian
- Chinese
- Dutch
- English (default language, when the user's one is not found)
- Esperanto
- French
- German
- Italian
- Japanese
- Norwegian
- Portugese (currently coming from the Bresilian translation)
- Spanish



~~~~~~~~~~~~~~~~~~~~~~~
2.0 HOWTO USE IT
~~~~~~~~~~~~~~~~~~~~~~~

  The jupload-VA.B.C.jar file contains the full distribution. Until 2.6.0, it contains also the compiled code (class
files), but not the source (java files). This is corrected in since 2.7.0: the class files are no more there, and
java files are given in the distribution jar file.
  Since 2.7.0, the distribution contains:
- the Sources, contained in the /wjhk/ directory
- The javadoc, contained in the /doc/ directory
- A sample, contained in the /www/ directory

  The compiled jar file, that should be used on production servers is /wwwroot/wjhk.jupload.jar

The given samples contains the server scripts described below.

~~~~~~~~~~~~~~~~~~~~~~~
3.0 Self Sign Applet.
~~~~~~~~~~~~~~~~~~~~~~~
  The applet must be signed, to be allowed to access to files on the client side. The best way is to sign the applet 
with a real way.
  Here is an explanation on how to sign the with a 'test' certificate. You _must_ do that after each modification of 
the jar package.
    - Generate Private/Public key set.
       keytool -genkey -alias "jupload" -validity 3600 -dname "CN=JUpload, OU=Testing/Demo, O=JUpload.SourceForge.net, L=SourceForge, S=SourceForge, C=SG"
    - List key set.
       keytool -list
    - Sign the Applet with the private key.
       jarsigner wjhk.jupload.jar jupload
    - Verify the jar file have being sign properly.
       jarsigner -verify wjhk.jupload.jar


~~~~~~~~~~~~~~~~~~~~~~
4.0	Server scripts
~~~~~~~~~~~~~~~~~~~~~~
The applet upload files to the server. Then, the server must handle the uploaded file. Here is an example, in java, 
of the way to handle the incoming file.

Here's a simple JSP script to store each uploaded file in the 'c:\temp' directory (see /wwwroot/pages/parseRequest.jsp for the last version)
---------------------------------------------------------------------
<%@ page language="java" import="java.io.*, java.sql.*, java.util.*" %>
<%@ page import="org.apache.commons.fileupload.*, org.apache.commons.fileupload.disk.*, org.apache.commons.fileupload.servlet.*" %>
<%
  response.setContentType("text/plain");
  try{
    // Get URL Parameters.
    Enumeration paraNames = request.getParameterNames();
    while (paraNames.hasMoreElements()) {
      String pname = (String)paraNames.nextElement();
      out.println(" ------------------------------ ");
      out.println(pname + " = " + request.getParameter(pname));
    }

    // Directory to store all the uploaded files
    String ourTempDirectory = "C:/Temp/";
    int ourMaxMemorySize  = 10000000;
    int ourMaxRequestSize = 2000000000;

	///////////////////////////////////////////////////////////////////////////////////////////////////////
	//The code below is directly taken from the jakarta fileupload common classes
	//All informations, and download, available here : http://jakarta.apache.org/commons/fileupload/
	///////////////////////////////////////////////////////////////////////////////////////////////////////
	
	// Create a factory for disk-based file items
	DiskFileItemFactory factory = new DiskFileItemFactory();
	
	// Set factory constraints
	factory.setSizeThreshold(ourMaxMemorySize);
	factory.setRepository(new File(ourTempDirectory));
	
	// Create a new file upload handler
	ServletFileUpload upload = new ServletFileUpload(factory);
	
	// Set overall request size constraint
	upload.setSizeMax(ourMaxRequestSize);
	
	// Parse the request
	List /* FileItem */ items = upload.parseRequest(request);
	// Process the uploaded items
	Iterator iter = items.iterator();
	FileItem fileItem;
    File fout;
	while (iter.hasNext()) {
	    fileItem = (FileItem) iter.next();
	
	    if (fileItem.isFormField()) {
	        //This should not occur, here.
	        out.println(" ------------------------------ ");
	        out.println(fileItem.getFieldName() + " = " + fileItem.getString());
	    } else {
	        //Ok, we've got a file. Let's process it.
	        //Again, for all informations of what is exactly a FileItem, please
	        //have a look to http://jakarta.apache.org/commons/fileupload/
	        //
	        out.println(" ------------------------------ ");
	        out.println("FieldName: " + fileItem.getFieldName());
	        out.println("File Name: " + fileItem.getName());
	        out.println("ContentType: " + fileItem.getContentType());
	        out.println("Size (Bytes): " + fileItem.getSize());
	        fout = new File(ourTempDirectory + (new File(fileItem.getName())).getName());
	        out.println("File Out: " + fout.toString());
	        // write the file
	        fileItem.write(fout);	        
	    }
	}
  }catch(Exception e){
    out.println("Exception e = " + e.toString());
  }
%>


---------------------------------------------------------------------
You can also read the file in php, using the $_FILES array. The uploaded filename is controled in the applet by the 
UploadPolicy.getUploadFilename method. The default behaviour is to return 'FileN' where N is the number of the 
uploaded file (for instance 0 to 4 when 5 files are uploaded). The CoppermineUploadPolicy changes that: here, files 
are uploaded one by one, and the uploaded file name is userpicture. The file is then managed in php by using the 
$_FILES['userpicture'] array.

See php doc for all details.


~~~~~~~~~~~~
4.0	FAQ:
~~~~~~~~~~~~
1) Any question on the java applet code  ?

  Please have a look to the java doc, available in the src/doc directory.

2) If your permissions are not setup properly the following Error messages
   will appear.

    - "File Chooser Exception: access denied (java... )"

First try to sign the applet. Don't forget to close your navigator, to make sure that your new applet is loaded.

3) You create a new jar on the web server, but your navigator only display the previous version.

 Don't forget to close your navigator, to make sure that your new applet is loaded.

 4) Any other problem  ?
 
Please take a look to the available documentation on http://jupload.sourceforge.net
