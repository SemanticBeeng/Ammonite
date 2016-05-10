package ammonite.hashbackup

import ammonite.hashbackup.intf.MountType.MountTypeVal
import ammonite.ops.Path
import ammonite.hashbackup.intf.BackDestType.{BackupDestVal, BackBlaze_B2}

/**
  *
  */
package object intf {

  import ammonite.ops.RelPath

  /**
    *
    */
  sealed trait BackupDir {

    def path: RelPath
  }

  sealed trait Local

  sealed trait Remote

  sealed trait Src

  sealed trait Dest

  // -----------------------------------------------------------
  /**
    * A "backup source directory"
    * Can be either local or remote and mounted locally under [[BackupRoots.backupSourceMountDirs]]
    */
  trait BackupSrcDir extends BackupDir with Src

  /**
    * A "backup destination directory" associated to a  [[BackupDestination]] of type directory
    */
  trait BackupDestDir extends BackupDir with Dest

  // -----------------------------------------------------------
  /**
    * A "backup directory" local to the machine that run the backup.
    * The backup process will copy the archives to a [[BackupRemoteDestDir]] when archive
    */
  trait BackupLocalDir extends BackupDir with Local

  trait BackupLocalSrcDir extends BackupSrcDir with Local

  // -----------------------------------------------------------
  /**
    * A "backup source directory"; Can be local or remotely mounted
    */
  trait BackupRemoteDir extends BackupDir with Remote

  /**
    * A "backup source directory"; Can be local or remotely mounted
    */
  trait BackupRemoteSrcDir extends BackupSrcDir with Remote

  trait BackupRemoteDestDir extends BackupDir with Remote with Dest

  // -----------------------------------------------------------
  trait Machine {

    def name : String

    def address : String

  }

  /**
    * Type of file system used to mount [[BackupRemoteSrcDir]]s from a [[Machine]] in a [[BackupDef]]
    */
  object MountType {

    sealed trait MountTypeVal

    case object CIFS extends MountTypeVal

    case object SSHFS extends MountTypeVal

    val values = Seq(CIFS, SSHFS)
  }

  /**
    * A backup definition.
    * Note: a backup can only be done from a single machine
    */
  trait BackupDef extends Mountable {

    def name: String

    def source: BackupSource

    def destinations: Seq[BackupDestination]

    def srcPaths : Seq[Path]

    def localPath : Path

    def mountPath : Path
  }

  // -----------------------------------------------------------
  trait User {
    def name: String

    def UID : Int

    def GID : Int
  }

  trait MountStatus {
    def result : Int

    def message : String
  }

  trait Mountable {

    def mountDirsAs(user : User) : List[(Path, MountStatus)]
  }

  trait BackupSource extends Mountable {

    def machine: Machine

    def dirs: Seq[BackupSrcDir]

    def mountType: MountTypeVal

    def paths : Seq[Path]
  }

  /**
    * A "backup destination"
    */
  trait BackupDestination {

    def machine: Machine

    def kind: BackupDestVal
  }

  /**
    *
    */
  object BackDestType {

    sealed trait BackupDestVal

    case object Directory extends BackupDestVal

    case object BackBlaze_B2 extends BackupDestVal

    val values = Seq(Directory, BackBlaze_B2)
  }

  /**
    *
    */
  trait BackupDestinationDir extends BackupDestination with Mountable {

    def dir: BackupRemoteDestDir

    def mountType : MountTypeVal

    def path: Path
  }

  trait BackupDestinationB2 extends BackupDestinationDir {

    def accountId: String
    def appKey : String
    def bucket : String
  }
}

