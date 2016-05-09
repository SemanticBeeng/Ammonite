package hashbackup

import ammonite.ops.RelPath

/**
  * A "backup source directory"; Can be local or remotely mounted
  */
case class BackupSrcDir(dir : RelPath) {
}
