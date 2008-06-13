Hi,

  Thanks for downloading JUpload !




You'll find in this file a description of the package content.


QUICK START :

The three main entry points are:
- Compiled applet: /wwwroot/wjhk.jupload.jar
- full sources :  /src  
- demo web site:  /wwwroot/index.html
  It contains links to all docs for the JUpload project.


PACKAGE CONTENT:

/src
  The full sources for the JUpload applet.

/wwwroot
  The copy of the JUpload web site, available as a link from the jupload sourceforge project.
  Its main content is:
  
	Main directories:
	  /wwwroot/doc/
	    The JUpload javadoc.
	  /wwwroot/wjhk.jupload.jar
	    THE COMPILED APPLET, that can you use directly on your web site. You should sign it with your own certificate.
	    See the /wwwroot/README.txt file
	  /wwwroot/pages/
	    Some samples for a J2EE server, like tomcat. The parseRequest.jsp show an example of managing uploaded file
	    in Java.

	Main files:
	  /wwwroot/advanced_js_demo.html
	    Allows you to test the applet with various parameters.
	  /wwwroot/applet-basic-picture.html
	    Example of the applet in picture mode (uses the PictureUploadPolicy upload policy)
	  /wwwroot/applet-basic.html
	    Basic applet demo.
	  /wwwroot/common.js
	    Some javascript used by the other pages.
	  /wwwroot/howto-customization.html
	    Documentation how you can make the applet match to your needs.
	  /wwwroot/howto-debug.html
	    How to analyse what's happening.
	  /wwwroot/howto-support.html
	    How and where to get support.
	  /wwwroot/howto-translation.html
	    We accept all translations !!!   ;-)
	  /wwwroot/index.html
	    ENTRY POINT FOR THE DEMO WEB SITE
	  /wwwroot/jakarta-commons-oro.jar
	    the jakarata library, signed by our demo certificate. Used in FTP mode only.
	  /wwwroot/nocache.php
	    Used to prevent caching of the jar file, by the sourceforge hosting.
	  /wwwroot/README.txt
	    A useful file!
	  /wwwroot/RELEASE-NOTES.txt
	    What's new in this relese (and full history)
	  /wwwroot/upload_dummy.php
	    A dummy php file, that can receive uploads. Can be used as a (very) simple sample in PHP
	  /wwwroot/wjhk.jupload.jar
	    THE APPLET, READY TO USE.

  
  You can use /wwwroot as the root for a J2EE application server, like Tomcat.