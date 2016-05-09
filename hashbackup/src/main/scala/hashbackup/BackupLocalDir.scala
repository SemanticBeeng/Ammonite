package hashbackup

import ammonite.ops.RelPath

/**
  * A "backup directory" local to the machine that run the backup.
  * The backup process will copy the archives to a [[BackupRemoteDestDir]] when archive
  */
case class BackupLocalDir(dir : RelPath) extends BackupDir {

}
