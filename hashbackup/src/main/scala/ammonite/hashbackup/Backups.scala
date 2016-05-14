package ammonite.hashbackup

import ammonite.ops._
import ammonite.hashbackup.intf.MountType.{LOCAL, SSHFS, CIFS}
import ammonite.hashbackup.intf.BackDestType
import ammonite.hashbackup.impl._
import ammonite.hashbackup.MachinesAndStorageBoxes._
/**
  *
  */
object Backups {

  /**
    *
    */
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

  val nickdsc_keys: String = "nickdsc_keys"

  /**
    *
    */
  def defineBigdatafierceBackups: Seq[BackupDef] = {
      val manual_backupsBackup = BackupDef(
        name = "manual_backups",
        source = BackupSource(machine = bigdatafierce,
          shareDir = BackupSrcDir('mydocs),
          dirs = Seq(BackupSrcDir("~backups")),
          mountType = CIFS),
        destinations = naz1AndHz1)

      val nickdsc_keysBackup = BackupDef(
        name = nickdsc_keys,
        source = BackupSource(machine = bigdatafierce,
          shareDir = BackupSrcDir('mydocs),
          dirs = Seq(BackupSrcDir("Keys")),
          mountType = CIFS),
        destinations = naz1AndHz1)

      /**
        * @todo: can I run the same backup with different sources??
        */
      val nickdsc_keys2Backup = BackupDef(
        name = nickdsc_keys,
        source = BackupSource(machine = bigdatafierce,
          shareDir = BackupSrcDir('Users),
          dirs = Seq(BackupSrcDir(RelPath("nickdsc/.ssh"))),
          mountType = CIFS),
        destinations = naz1AndHz1)

      val personal_and_corpBackup = BackupDef(
        name = "personal_and_corp",
        source = BackupSource(machine = bigdatafierce,
          shareDir = BackupSrcDir('mydocs),
          dirs = Seq(BackupSrcDir("Personal")),
          mountType = CIFS),
        destinations = naz1AndHz1)

      val knowledgerepoBackup = BackupDef(
        name = "knowledgerepo",
        source = BackupSource(machine = bigdatafierce,
          shareDir = BackupSrcDir('mydocs),
          dirs = Seq(BackupSrcDir(RelPath("repos/knowledgerepo"))),
          mountType = CIFS),
        destinations = naz1AndHz1)

      val project_sourcesBackup = BackupDef(
        name = "project_sources",
        source = BackupSource(machine = bigdatafierce,
          shareDir = BackupSrcDir('projects),
          dirs = Seq(BackupSrcDir(RelPath("."))),
          mountType = CIFS),
        destinations = naz1AndHz1)

      val nickdsc_user_homeBackup = BackupDef(
        name = "nickdsc_user_home",
        source = BackupSource(machine = bigdatafierce,
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
        destinations = naz1AndHz1)

      val virtual_machinessBackup = BackupDef(
        name = "virtual_machines",
        source = BackupSource(machine = bigdatafierce,
          shareDir = BackupSrcDir('vms),
          dirs = Seq(BackupSrcDir(RelPath("VirtualBox VMs/DevShell-Ubuntu")),
            BackupSrcDir(RelPath("VirtualBox VMs/MacKnowledge")),
            BackupSrcDir(RelPath("VirtualBox VMs/disks"))),
          mountType = CIFS),
        destinations = naz1AndHz1)

      Seq(manual_backupsBackup,
        nickdsc_keysBackup,
        nickdsc_keys2Backup,
        personal_and_corpBackup,
        knowledgerepoBackup,
        project_sourcesBackup,
        nickdsc_user_homeBackup,
        virtual_machinessBackup)
  }

  /**
    *
    */
  def defineBigdatafierce_vmBackups: Seq[BackupDef] = {

      val pure_dataBackup = BackupDef(
        name = "pure_data",
        source = BackupSource(machine = bigdatafierce_vm,
          shareDir = BackupSrcDir('datascience),
          dirs = Seq(BackupSrcDir('marketdata)),
          mountType = LOCAL),
        destinations = naz1AndHz1)

      val nickdsc_user_homeBackup = BackupDef(
        name = "nickdsc_user_home",
        source = BackupSource(machine = bigdatafierce_vm,
          shareDir = BackupSrcDir(RelPath("home/nickdsc")),
          dirs = Seq(BackupSrcDir(RelPath(".bash")),
            BackupSrcDir(RelPath(".vim")),
            BackupSrcDir(RelPath(".smbcredentials")),
            BackupSrcDir(RelPath(".IntelliJIdea15"))),
          mountType = LOCAL),
        destinations = naz1AndHz1)

      val nickdsc_keysBackup = BackupDef(
        name = nickdsc_keys,
        source = BackupSource(machine = bigdatafierce_vm,
          shareDir = BackupSrcDir(RelPath("home/nickdsc")),
          dirs = Seq(BackupSrcDir(RelPath(".ssh"))),
          mountType = LOCAL),
        destinations = naz1AndHz1)

      Seq(pure_dataBackup, nickdsc_user_homeBackup, nickdsc_keysBackup)
  }

  /**
    *
    */
  def defineBigdatafierceHZ1_Backups: Seq[BackupDef] = {

    val domainspec_keyssBackup = BackupDef(
      name = "domainspec_keys",
      source = BackupSource(machine = bigdatafierce_hz1,
        shareDir = BackupSrcDir(RelPath("home/domainspec")),
        dirs = Seq(BackupSrcDir(".ssh")),
        mountType = LOCAL),
      destinations = Seq(storagebox_hz1_destBackup))

    val domainspec_user_homeBackup = BackupDef(
      name = "domainspec_user_home",
      source = BackupSource(machine = bigdatafierce_hz1,
        shareDir = BackupSrcDir(RelPath("home/domainspec")),
        dirs = Seq(BackupSrcDir(".bash")),
        mountType = LOCAL),
      destinations = Seq(storagebox_hz1_destBackup))

    Seq(domainspec_keyssBackup,domainspec_user_homeBackup)
  }
}

/**
  *
  */
object MakeBackups extends App {

  override def main(args: Array[String]) {

    case class Thing(myFieldA: Int, myFieldB: String)
    case class Big(i: Int, b: Boolean, str: String, c: Char, t: Thing)

//    import upickle.default._
//    import upickle._
//    write(Thing(1, "gg"))
//    write(Big(1, true, "lol", 'Z', Thing(7, "")))

    val allBackups1 = Backups.defineBigdatafierceBackups

    val allBackups2 = Backups.defineBigdatafierce_vmBackups

    val allBackups3 = Backups.defineBigdatafierceHZ1_Backups

    OSHandler.ensureRootsGoodToMountUnder

    val nickdsc = User("nickdsc", 1001, 1002)

    allBackups1 foreach { backup =>

      println(backup)

      //      backup.mountSourcePaths(nickdsc)
      //
      //      backup.mountRemoteDestPaths(nickdsc)
    }

    allBackups2 foreach { backup =>

      println(backup)

      //      backup.mountSourcePaths(nickdsc)
      //
      //      backup.mountRemoteDestPaths(nickdsc)
    }

    allBackups3 foreach { backup =>

      println(backup)

      //      backup.mountSourcePaths(nickdsc)
      //
      //      backup.mountRemoteDestPaths(nickdsc)
    }
  }

}