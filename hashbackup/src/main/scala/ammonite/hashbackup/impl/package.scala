package ammonite.hashbackup

import ammonite.hashbackup.intf.{BackupDef, BackupDir}
import ammonite.hashbackup.intf.MountType._
import ammonite.ops.{RelPath, BasePath, Path}
import ammonite.hashbackup.intf.BackDestType.BackupDestVal

import scalaz.{-\/, \/-, \/}

/**
  *
  */
package object impl {

  case class BackupSrcDir[P <: BasePath](path : P)
    extends intf.BackupSrcDir[P] {
  }

  case class BackupLocalDir(path : Path)
    extends intf.BackupLocalDir {

  }

  case class BackupLocalSrcDir(path : Path)
    extends intf.BackupLocalSrcDir {

  }

  case class BackupRemoteSrcDir(path : RelPath)
    extends intf.BackupRemoteSrcDir {

  }

  case class BackupRemoteDestDir(path: RelPath)
    extends intf.BackupRemoteDestDir {

  }

  /**
    *
    */
  case class Machine(name : String, address : String)
    extends intf.Machine

  /**
    *
    */
  case class BackupSource[P <: BasePath](machine: Machine, shareDir: intf.BackupSrcDir[P],
                                        dirs: Seq[intf.BackupSrcDir[RelPath]], mountType: MountTypeVal)
    extends intf.BackupSource[P] {

    def shareName: String = {

      //Predef.assert(shareDir.path.segments.length == 1)

      (if(mountType == LOCAL)
        //root/
          shareDir.path
      else
        shareDir.path.toString.replace('/', '$')).toString

    }

    def localMountPath = {
      if (mountType == LOCAL) {
        // Expect a root relative path
        Predef.assert(shareDir.path.isInstanceOf[Path])
        /*root / */shareDir.path.asInstanceOf[Path]
      }
      else {
        Predef.assert(shareDir.path.isInstanceOf[RelPath])
        impl.machinePath(BackupRoots.backupSourceMountDirs, machine) / shareDir.path.asInstanceOf[RelPath]
      }
    }

    /**
      * @example
      * DestName semanticbrainex_nas1
      * Type Dir
      * Dir /mnt/backups/bckp_dests/semanticbrainex_nas1/Backup/manual_backup
      *
      * DestName StorageBox_HZ1
      * Type Dir
      * Dir /mnt/backups/bckp_dests/storagebox_hz1/Backup/manual_backup
      */
    def generateDestConfContent = {

      def genDestEntryFor(d: intf.BackupDestination)(implicit backup: BackupDef[_]): String = {

        d match {
          case d:intf.BackupDestinationDir =>
            s"DestName ${d.machine.name}\n " +
              s"Type Dir\n" +
              s"Dir ${d.localMountPath}/${backup.name}\n\n"

          case d:intf.BackupDestinationB2 =>
            s"DestName ${d.machine.name}\n " +
              s"Type B2\n" +
              s"Dir ${d.localMountPath}/${backup.name}\n\n"

          case _ =>
            Predef.assert(assertion = false, s"Unexpected backup destination $d")
            s"Unknown destination $d"
        }
      }

      destinations map {d => genDestEntryFor(d)}
    }


    override def toString = {
        def sourcePaths = dirs map {d => shareName + "/" + d.path}

        s"BackupSource(machine = $machine, mountType = $mountType, sourcePaths = $sourcePaths, " +
          s"localMountPath = $localMountPath)" }
      }

  /**
    *
    */
  case class BackupDestinationDir(machine: Machine, kind: BackupDestVal, dir: BackupRemoteDestDir, mountType : MountTypeVal)
    extends intf.BackupDestinationDir {

    def path : Path  = machinePath(BackupRoots.backupDirs, machine) / dir.path

    override def shareDir: BackupDir[RelPath] = dir.asInstanceOf[BackupDir[RelPath]]

    override def shareName: String = shareDir.path.toString

    override def localMountPath: Path = {
      Predef.assert(shareDir.path.isInstanceOf[RelPath])
      impl.machinePath(BackupRoots.backupDestinationDirs, machine) / shareDir.path.segments.last
    }

    override def toString = s"BackupDestinationDir(machine = $machine, mountType = $mountType, " +
      s"localMountPath = $localMountPath)"

  }

  /**
    * Backup definition
    */
  sealed case class BackupDef[P <: BasePath](name: String,
                       source: intf.BackupSource[P],
                       destinations: Seq[intf.BackupDestination])
    extends intf.BackupDef[P] {


    /**
      * Full path to the (local) "backup directory"
      */
    def localPath : Path  = machinePath(BackupRoots.backupDirs, source.machine) / name

    /**
      * Full path to the (local) directory where the finished backup is mounted
      */
    def mountPath : Path = machinePath(BackupRoots.backupMountDirs, source.machine) / name

    def sourcePaths: Seq[Path] = source.dirs map { d => source.localMountPath / d.path }

    import ammonite.hashbackup.OSHandler._

    def mountSourcePaths(user: User): \/[Seq[Path], \/[Path, intf.MountError]] = {

      val result: \/[Path, intf.MountError] = mountDirAs[P](source, user)
      if(result.isLeft) {
        -\/(sourcePaths)
      } else {
        \/-(result)
      }
    }

    /**
      *
      */
    def mountRemoteDestPaths(user: User): Seq[\/[BasePath, intf.MountError]] = {

      destinations map {d => mountDirAs(d, user)}
    }

    /**
      *
      *
      */
    def execute() = {
      executeBackup(this)
    }

    override def toString = s"BackupDef(name = $name; localPath = $localPath; " +
      s"source = $source; destinations = $destinations; mountPath =$mountPath)"

  }

  case class MountError(result : Int, message : String) extends intf.MountError {

  }

  case class User(name : String, UID :Int, GID : Int) extends intf.User

  val machinePath = (root: Path, machine: intf.Machine) => root / machine.name
}
