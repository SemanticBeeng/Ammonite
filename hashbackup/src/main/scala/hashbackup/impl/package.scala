package hashbackup

import ammonite.ops.RelPath

/**
  *
  */
package object impl {

  case class BackupSrcDir(dir : RelPath) extends intf.BackupSrcDir {
  }

  case class BackupLocalDir(dir : RelPath) extends intf.BackupLocalDir {

  }

  case class BackupRemoteSrcDir(dir : RelPath) extends intf.BackupRemoteSrcDir {

  }

  case class BackupRemoteDestDir(dir: RelPath) extends intf.BackupRemoteDestDir {

  }

}
