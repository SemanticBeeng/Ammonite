package ammonite.hashbackup

import ammonite.hashbackup.intf.MountType.MountTypeVal
import ammonite.ops.{Path, RelPath}
import ammonite.hashbackup.intf.BackDestType.BackupDestVal
import ammonite.hashbackup.intf.MountStatus

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

    def pathsToMount : Seq[Path] = dirs map (dir => machinePath(BackupRoots.backupSourceMountDirs, machine) / dir.path)
  }

  /**
    *
    */
  case class BackupDestinationDir(machine: Machine, kind: BackupDestVal, dir: BackupRemoteDestDir, mountType : MountTypeVal)
    extends intf.BackupDestinationDir {

    def path : Path  = machinePath(BackupRoots.backupDirs, machine) / dir.path

    override def pathsToMount: Seq[Path] = List(path).toSeq
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
    def srcPaths : Seq[Path] = source.pathsToMount

    /**
      * Full path to the (local) "backup directory"
      */
    def localPath : Path  = machinePath(BackupRoots.backupDirs, source.machine) / name

    def mountPath() : Path = machinePath(BackupRoots.backupMountDirs, source.machine) / name

    override def pathsToMount : Seq[Path] = srcPaths//(List(srcPaths()) ::: List(mountPath())).toSeq
  }

  case class User(name : String, UID :Int, GID : Int) extends intf.User

  val machinePath = (root: Path, machine: intf.Machine) => root / machine.name

}
