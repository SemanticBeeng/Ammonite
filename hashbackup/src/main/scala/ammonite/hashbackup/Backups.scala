package ammonite.hashbackup

import ammonite.hashbackup.intf.MountType.{SSHFS, CIFS}
import ammonite.hashbackup.intf.BackDestType
import ammonite.hashbackup.impl._
import ammonite.hashbackup.MachinesAndStorageBoxes._

/**
  *
  */
object Backups {

  val semanticbrainex_nas1_destBackup = BackupDestinationDir(
    machine = semanticbrainex_nas1,
    kind = BackDestType.Directory,
    dir = BackupRemoteDestDir("Backup"),
    mountType = SSHFS)

  val manualBackups = new BackupDef(
    name = "manual_backups",
    source = new BackupSource(machine = bigdatafierce,
      shareDir = BackupSrcDir('mydocs),
      dirs = Seq(BackupSrcDir("~backups")),
      mountType = CIFS),
    destinations= Seq(semanticbrainex_nas1_destBackup))

}

object MakeBackups extends App {

  override def main(args: Array[String]) {

    OSHandler.ensureRootsGoodToMountUnder

    val backup = Backups.manualBackups

    println(s"Backup local ${backup.localPath}" )
    println(s"Backup source paths ${backup.source.dirs}")
    println(s"Backup mount ${backup.mountPath}" )

    val nickdsc = new User("nickdsc", 1001, 1002)

    backup.mountSourcePaths(nickdsc)

    backup.mountRemoteDestPaths(nickdsc)
  }

}