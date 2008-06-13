<?php
  // The apache on SF does not have mod_headers or mod_expires enabled
  // Therefore, we need this silly workaround in order to disable
  // caching of jar files.
  $name = $_SERVER['PATH_TRANSLATED'];
  $fp = fopen($name, 'rb');
  header('Pragma: no-cache');
  header('Cache-Control: no-store, no-cache, must-revalidate, post-check=0, pre-check=0');
  fpassthru($fp);
  exit;