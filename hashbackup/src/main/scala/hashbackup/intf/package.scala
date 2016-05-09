package hashbackup

/**
  *
  */
package object intf {

  import ammonite.ops.RelPath

  /**
    *
    */
  sealed trait BackupDir {

    def dir : RelPath
  }

  sealed trait Local
  sealed trait Remote
  sealed trait Src
  sealed trait Dest

  // -----------------------------------------------------------
  trait BackupSrcDir extends BackupDir with Src

  trait BackupDestDir extends BackupDir with Dest

  // -----------------------------------------------------------
  trait BackupLocalDir extends BackupDir with Local

  trait BackupLocalSrcDir extends BackupDir with Src with Local

  // -----------------------------------------------------------
  trait BackupRemoteDir extends BackupDir with Remote

  trait BackupRemoteSrcDir extends BackupDir with Src with Remote

  trait BackupRemoteDestDir extends BackupDir with Remote with Dest
}
