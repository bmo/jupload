package wjhk.jupload2.upload;

import wjhk.jupload2.policies.UploadPolicy;
import wjhk.jupload2.gui.JUploadPanel;
import java.util.concurrent.*;

import netscape.javascript.JSObject;

/* queue outcalls to the browser, handle them one by one. */


public class JavascriptOutcaller extends Thread {


  /**
   * Reference to the current upload policy.
   */

  private UploadPolicy uploadPolicy = null;

  /**
   * Reference to the main panel of the applet.
   */
  private JUploadPanel jUploadPanel = null;

  public JavascriptOutcaller(UploadPolicy uploadPolicy,
                           JUploadPanel theJUploadPanel) {
    this.uploadPolicy = uploadPolicy;
    this.jUploadPanel = theJUploadPanel;
    this.uploadPolicy.displayDebug("JavascriptOutcaller - created ",20);
    //Let's start our thread.
    this.start();
  }

   private BlockingQueue<String> queue =
     new LinkedBlockingQueue<String>();

     public void queue_callback(String js_to_eval) {
        this.uploadPolicy.displayDebug("JavascriptOutcaller - queueing callback "+js_to_eval,20);
       queue.add(js_to_eval);
     }

     public void run() {
       String js_to_eval;
       Object return_val;
        while (true)
           try {
              js_to_eval=queue.take();
             /* A JavaScript expression was specified. Execute it. */
              this.uploadPolicy.displayDebug("JavascriptOutcaller - servicing thread outcalling with "+js_to_eval,20);
              return_val =  JSObject.getWindow(this.uploadPolicy.getApplet()).eval(js_to_eval);
                //return return_val;
           }
           catch (Exception ee) {
                // Oops, no navigator. We are probably in debug mode, within
                // eclipse for instance.
                this.uploadPolicy.displayErr(ee);
            }
     }

}
