package hashbackup

import ammonite.ops.RelPath

/**
  *
  */
sealed trait BackupDir {

  def dir : RelPath
}

trait BackupSrcDir extends BackupDir

trait BackupDestDir extends BackupDir

trait BackupRemoteDir extends BackupDir
