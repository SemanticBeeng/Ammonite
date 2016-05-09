package hashbackup

import ammonite.ops.RelPath
import hashbackup.intf.BackupDir

/**
  *
  */
package object impl {

  /**
    * A "backup directory" local to the machine that run the backup.
    * The backup process will copy the archives to a [[BackupRemoteDestDir]] when archive
    */
  case class BackupLocalDir(dir : RelPath) extends intf.BackupLocalDir {

  }
  /**
    * A "backup source directory"; Can be local or remotely mounted
    */
  case class BackupRemoteSrcDir(dir : RelPath) extends intf.BackupSrcDir {

  }

  /**
    * A "backup directory"
    */
  case class BackupRemoteDestDir(dir: RelPath) extends intf.BackupRemoteDestDir

}
