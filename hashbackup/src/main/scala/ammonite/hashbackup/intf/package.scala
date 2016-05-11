package ammonite.hashbackup

import ammonite.ops.Path
import ammonite.hashbackup.intf.MountType.MountTypeVal
import ammonite.hashbackup.intf.BackDestType.BackupDestVal

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

    case object LOCAL extends MountTypeVal

    case object CIFS extends MountTypeVal

    case object SSHFS extends MountTypeVal

    val values = Seq(CIFS, SSHFS)
  }

  /**
    * A backup definition.
    * Note: a backup can only be done from a single machine
    */
  trait BackupDef {

    def name: String

    def source: BackupSource

    def destinations: Seq[BackupDestination]
  }

  // -----------------------------------------------------------
  trait User {
    def name: String

    def UID : Int

    def GID : Int
  }

  trait MountError {
    def result : Int

    def message : String
  }

  trait Mountable {

    def machine: Machine

    def shareDir : BackupDir

    def mountType: MountTypeVal

    def shareName : String

    def localMountPath : Path
  }

  /**
    * Cannot mix remote and local [[BackupSrcDir]] in the same [[BackupSource]]
    */
  trait BackupSource extends Mountable {

    def machine: Machine

    /**
      * The "share" to backup from.
      * <ul>
      *   <li> a real share if [[machine]] != backup machine ; type = [[BackupRemoteSrcDir]]
      *   <li> a local directory if [[machine]] == backup machine ; type = [[BackupLocalSrcDir]]
      * </ul>
      */
    def shareDir : BackupSrcDir

    /**
      * The children of [[shareDir]] that are backed-up
      */
    def dirs: Seq[BackupSrcDir]

    def mountType: MountTypeVal

    //def pathsToMount : Seq[Path]
  }

  /**
    * A "backup destination"
    */
  trait BackupDestination extends Mountable {

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
  trait BackupDestinationDir extends BackupDestination {

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

