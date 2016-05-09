package hashbackup

import ammonite.ops._

/**
  * Root backup directories (sources, locals. destinations and mount points) for backups executed on a/this machine
  */
object BackupRoots {

  /**
    * Root folder of all backups executed on this machine (local copies for [[hashbackup.intf.BackupLocalDir]] of all the backups)
    */
  val backupDirs: BasePath = root/'data/'bckp_dirs

  /**
    * Root folder where to mount all the [[hashbackup.intf.BackupRemoteSrcDir]]s for data to be backed-up from other machines
    */
  val backupSourceMountDirs: BasePath =root/'mnt/'backups/'bckp_srcs

  /**
    * Root folder where to mount all the [[hashbackup.intf.BackupRemoteDestDir]]s for all the backups
    */
  val backupDestinationDirs : BasePath = root/'mnt/'backups/'bckp_dests

  /**
    * Root folder where to mount all the backups living on [[hashbackup.intf.BackupRemoteDestDir]]
    */
  val backupMountDirs : BasePath = root/'mnt/'backups/'bckp_mnts
}
