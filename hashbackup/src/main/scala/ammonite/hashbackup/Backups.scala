package ammonite.hashbackup

import ammonite.hashbackup.intf.MountType.{SSHFS, CIFS}
import ammonite.hashbackup.intf.BackDestType
import ammonite.hashbackup.impl._
import ammonite.hashbackup.MachinesAndStorageBoxes._
import ammonite.ops.RelPath

/**
  *
  */
object Backups {

  val semanticbrainex_nas1_destBackup = BackupDestinationDir(
    machine = semanticbrainex_nas1,
    kind = BackDestType.Directory,
    dir = BackupRemoteDestDir("Backup"),
    mountType = SSHFS)

  val storagebox_hz1_destBackup = BackupDestinationDir(
    machine = storagebox_hz1,
    kind = BackDestType.Directory,
    dir = BackupRemoteDestDir("Backup"),
    mountType = SSHFS)

  private val naz1AndHz1: Seq[BackupDestinationDir] = Seq(semanticbrainex_nas1_destBackup, storagebox_hz1_destBackup)

  val manual_backupsBackup = new BackupDef(
    name = "manual_backups",
    source = new BackupSource(machine = bigdatafierce,
      shareDir = BackupSrcDir('mydocs),
      dirs = Seq(BackupSrcDir("~backups")),
      mountType = CIFS),
    destinations= naz1AndHz1)

  private val nickdsc_keys: String = "nickdsc_keys"
  val nickdsc_keysBackup = new BackupDef(
    name = nickdsc_keys,
    source = new BackupSource(machine = bigdatafierce,
      shareDir = BackupSrcDir('mydocs),
      dirs = Seq(BackupSrcDir("Keys")),
      mountType = CIFS),
    destinations= naz1AndHz1)

  /**
    * @todo: can I run the same backup with different sources??
    */
  val nickdsc_keys2Backup = new BackupDef(
    name = nickdsc_keys,
    source = new BackupSource(machine = bigdatafierce,
      shareDir = BackupSrcDir('Users),
      dirs = Seq(BackupSrcDir(RelPath("nickdsc/.ssh"))),
      mountType = CIFS),
    destinations= naz1AndHz1)

  val personal_and_corpBackup = new BackupDef(
    name = "personal_and_corp",
    source = new BackupSource(machine = bigdatafierce,
      shareDir = BackupSrcDir('mydocs),
      dirs = Seq(BackupSrcDir("Personal")),
      mountType = CIFS),
    destinations= naz1AndHz1)

  val knowledgerepoBackup = new BackupDef(
    name = "knowledgerepo",
    source = new BackupSource(machine = bigdatafierce,
      shareDir = BackupSrcDir('mydocs),
      dirs = Seq(BackupSrcDir(RelPath("repos/knowledgerepo"))),
      mountType = CIFS),
    destinations= naz1AndHz1)

  val project_sourcesBackup = new BackupDef(
    name = "project_sources",
    source = new BackupSource(machine = bigdatafierce,
      shareDir = BackupSrcDir('projects),
      dirs = Seq(BackupSrcDir(RelPath("."))),
      mountType = CIFS),
    destinations= naz1AndHz1)

  val nickdsc_user_homeBackup = new BackupDef(
    name = "nickdsc_user_home",
    source = new BackupSource(machine = bigdatafierce,
      shareDir = BackupSrcDir('Users),
      dirs = Seq(BackupSrcDir(RelPath("nickdsc/.vagrant.d")),
        BackupSrcDir(RelPath("nickdsc/.x2go")),
        BackupSrcDir(RelPath("nickdsc/.VirtualBox")),
        BackupSrcDir(RelPath("nickdsc/Desktop")),
        BackupSrcDir(RelPath("nickdsc/.bash_history")),
        BackupSrcDir(RelPath("nickdsc/.gitconfig")),
        BackupSrcDir(RelPath("nickdsc/.viminfo")),
        BackupSrcDir(RelPath("nickdsc/default"))
      ),
      mountType = CIFS),
    destinations= naz1AndHz1)

  val virtual_machinessBackup = new BackupDef(
    name = "virtual_machines",
    source = new BackupSource(machine = bigdatafierce,
      shareDir = BackupSrcDir('vms),
      dirs = Seq(BackupSrcDir(RelPath("VirtualBox VMs/DevShell-Ubuntu")),
          BackupSrcDir(RelPath("VirtualBox VMs/MacKnowledge")),
          BackupSrcDir(RelPath("VirtualBox VMs/disks"))),
      mountType = CIFS),
    destinations= naz1AndHz1)

  val allBackups = Seq(manual_backupsBackup,
    nickdsc_keysBackup,
    nickdsc_keys2Backup,
    personal_and_corpBackup,
    knowledgerepoBackup,
    project_sourcesBackup,
    nickdsc_user_homeBackup,
    virtual_machinessBackup)
}

object MakeBackups extends App {

  override def main(args: Array[String]) {

    OSHandler.ensureRootsGoodToMountUnder

    val nickdsc = new User("nickdsc", 1001, 1002)

    Backups.allBackups map { backup =>

      println(backup)

//      backup.mountSourcePaths(nickdsc)
//
//      backup.mountRemoteDestPaths(nickdsc)
    }
  }
}