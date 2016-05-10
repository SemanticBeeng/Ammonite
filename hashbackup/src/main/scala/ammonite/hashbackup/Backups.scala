package ammonite.hashbackup

import ammonite.ops._
import ammonite.hashbackup.intf.BackDestType
import ammonite.hashbackup.impl.{BackupSrcDir, BackupDef, BackupRemoteDestDir, BackupDestinationDir}
import ammonite.hashbackup.MachinesAndStorageBoxes._

/**
  *
  */
object Backups {

  val semanticbrainex_nas1_destBackup = BackupDestinationDir(
    machine = semanticbrainex_nas1,
    kind = BackDestType.Directory,
    dir = BackupRemoteDestDir("Backup"))

  val b1 = new BackupDef(
    srcMachine = bigdatafierce,
    srcDirs = Seq(BackupSrcDir('mydocs/"~backups")),
    name = "manual_backups",
    destinations= Seq(semanticbrainex_nas1_destBackup))

}
