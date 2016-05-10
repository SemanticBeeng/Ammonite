package ammonite.hashbackup

import ammonite.hashbackup.impl.Util._
import ammonite.hashbackup.intf
import ammonite.hashbackup.intf.MountType.MountTypeVal
import ammonite.ops.{Path, RelPath}
import ammonite.hashbackup.intf.BackDestType.BackupDestVal
import ammonite.hashbackup.intf.{MountStatus, User, BackupDestination}

/**
  *
  */
package object impl {

  case class BackupSrcDir(path : RelPath)
    extends intf.BackupSrcDir {
  }

  case class BackupLocalDir(path : RelPath)
    extends intf.BackupLocalDir {

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
  case class BackupSource(machine: Machine, dirs: Seq[intf.BackupSrcDir], mountType: MountTypeVal)
    extends intf.BackupSource {

    def paths : Seq[Path] = dirs map (dir => machinePath(BackupRoots.backupSourceMountDirs, machine) / dir.path)

    override def mountDirsAs(user: intf.User): List[(Path, MountStatus)] = ???
  }

  /**
    *
    */
  case class BackupDestinationDir(machine: Machine, kind: BackupDestVal, dir: BackupRemoteDestDir, mountType : MountTypeVal)
    extends intf.BackupDestinationDir {

    def path : Path  = machinePath(BackupRoots.backupDirs, machine) / dir.path

    override def mountDirsAs(user: intf.User): List[(Path, MountStatus)] = ???
  }

  /**
    * Backup definition
    */
  case class BackupDef(name: String,
                       source: intf.BackupSource,
                       destinations: Seq[intf.BackupDestination])
    extends intf.BackupDef {

    /**
      * Full paths to be backed-up
      */
    def srcPaths : Seq[Path] = source.paths

    /**
      * Full path to the (local) "backup directory"
      */
    def localPath : Path  = machinePath(BackupRoots.backupDirs, source.machine) / name

    def mountPath : Path = machinePath(BackupRoots.backupMountDirs, source.machine) / name

    def mountDirsAs(user: intf.User) : List[(Path, MountStatus)] = {
      source.mountDirsAs(user) //+
//      destinations map {d => d.isInstanceOf[BackupDestinationDir] ? d.asInstanceOf[BackupDestinationDir].mountDirsAs
//        : }
//
//
//      ? dest.asInstanceOf[BackupDestinationDir]
//        .mountDirsAs : false }
    }

  }

  case class User(name : String, UID :Int, GID : Int) extends intf.User

  object Util {

    /**
      *
      */
    def machinePath(root: Path, machine: intf.Machine): Path = root / machine.name
  }
}
