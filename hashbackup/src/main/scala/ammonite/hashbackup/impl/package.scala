package ammonite.hashbackup

import ammonite.hashbackup.impl.Util._
import ammonite.hashbackup.intf.MountType.MountTypeVal
import ammonite.ops.{Path, RelPath}
import ammonite.hashbackup.intf.BackDestType.BackupDestVal
import ammonite.hashbackup.intf.BackupDestination

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

  }

  /**
    *
    */
  case class BackupDestinationDir(machine: Machine, kind: BackupDestVal, dir: BackupRemoteDestDir, mountType : MountTypeVal)
    extends intf.BackupDestinationDir {

    def path : Path  = machinePath(BackupRoots.backupDirs, machine) / dir.path
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
    def backupDirPath : Path  = machinePath(BackupRoots.backupDirs, source.machine) / name

    def mountSourceDirs: Unit = {

    }

  }

  object Util {

    /**
      *
      */
    def machinePath(root: Path, machine: intf.Machine): Path = root / machine.name
  }
}
