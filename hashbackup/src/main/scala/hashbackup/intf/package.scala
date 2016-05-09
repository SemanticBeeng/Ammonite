package hashbackup

import hashbackup.intf.BackDestType.{BackupDestVal, BackBlaze_B2}

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

    def ipAddress : String

  }

  trait BackupDef {

    def srcMachine : Machine

    def srcDirs: Seq[BackupSrcDir]

    def name: String

    //def destinations: Seq[(BackupDestination, BackupRemoteDir)]
    def destinations: Seq[BackupDestination]
  }

  // -----------------------------------------------------------

  /**
    * A "backup destination"
    */
  trait BackupDestination {

    def name: String

    def kind: BackupDestVal
  }

  object BackDestType {

    sealed trait BackupDestVal

    case object Directory extends BackupDestVal

    case object BackBlaze_B2 extends BackupDestVal

    val values = Seq(Directory, BackBlaze_B2)
  }


  trait BackupDestinationDir extends BackupDestination {

    def dir: BackupRemoteDestDir
  }

  trait BackupDestinationB2 extends BackupDestinationDir {

    def accountId: String
    def appKey : String
    def bucket : String
  }
}

