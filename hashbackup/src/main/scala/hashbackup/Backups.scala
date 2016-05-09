package hashbackup

import ammonite.ops._
import hashbackup.MachinesAndStorageBoxes._
import hashbackup.impl._
import hashbackup.intf.BackDestType

/**
  *
  */
object Backups {

  val dest_semanticbrainex_nas1 = BackupDestinationDir(
    name = semanticbrainex_nas1.name,
    kind = BackDestType.Directory,
    dir = BackupRemoteDestDir("Backup"))

  val b1 = new BackupDef(
    srcMachine = bigdatafierce,
    srcDirs = Seq(BackupSrcDir('mydocs/'backups)),
    name = "manual_backups",
    destinations= Seq(dest_semanticbrainex_nas1))


}
