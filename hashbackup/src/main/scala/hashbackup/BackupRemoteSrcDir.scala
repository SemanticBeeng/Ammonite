package hashbackup

import ammonite.ops._

/**
  * A "backup source directory"; Can be local or remotely mounted
  */
case class BackupRemoteSrcDir(dir : RelPath) extends BackupSrcDir(dir) {

}
