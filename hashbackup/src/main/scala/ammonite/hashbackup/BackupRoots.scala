package ammonite.hashbackup

import ammonite.ops._

/**
  * Root backup directories (sources, locals. destinations and mount points) for backups executed on a/this machine
  */
object BackupRoots {

  /**
    * Root folder of all backups executed on this machine (local copies for [[intf.BackupLocalDir]] of all the backups)
    */
  val backupDirs: Path = root/'data/'bckp_dirs

  /**
    * Root folder where to mount all the [[intf.BackupRemoteSrcDir]]s for data to be backed-up from other machines
    */
  val backupSourceMountDirs: Path =root/'mnt/'backups/'bckp_srcs

  /**
    * Root folder where to mount all the [[intf.BackupRemoteDestDir]]s for all the backups
    */
  val backupDestinationDirs : Path = root/'mnt/'backups/'bckp_dests

  /**
    * Root folder where to mount all the backups living on [[intf.BackupRemoteDestDir]]
    */
  val backupMountDirs : Path = root/'mnt/'backups/'bckp_mnts

  val all = List(backupDirs, backupSourceMountDirs, backupDestinationDirs, backupMountDirs)
}
