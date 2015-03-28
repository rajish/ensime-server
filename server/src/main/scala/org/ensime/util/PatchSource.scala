package org.ensime.util

import org.ensime.model.{ PatchDelete, PatchInsert, PatchOp, PatchReplace }

import scala.reflect.internal.util.{ BatchSourceFile, SourceFile }

object PatchSource {

  def applyOperations(
    s: SourceFile, ops: List[PatchOp]): SourceFile = {
    val result = applyOperations(s.content, ops)
    new BatchSourceFile(s.file, result)
  }

  def applyOperations(
    input: String, ops: List[PatchOp]): String = {
    val chars = new Array[Char](input.length)
    input.getChars(0, input.length, chars, 0)
    val result = applyOperations(chars, ops)
    new String(result)
  }

  def applyOperations(
    input: Array[Char], ops: List[PatchOp]): Array[Char] = {
    val newLen = input.length + netLengthChange(ops)
    val result = new Array[Char](newLen)
    var offset = 0
    var srcCursor = 0
    for (op <- ops) {
      val i = op.start
      val copyLen = i - srcCursor
      System.arraycopy(input, srcCursor, result,
        srcCursor + offset, copyLen)
      srcCursor += copyLen
      op match {
        case PatchInsert(start, text) =>
          text.getChars(0, text.length, result, start + offset)
          offset += text.length
        case PatchReplace(start, end, text) =>
          text.getChars(0, text.length, result, start + offset)
          offset += text.length - (end - start)
          srcCursor += end - start
        case PatchDelete(start, end) =>
          offset -= end - start
          srcCursor += end - start
      }
    }
    val copyLen = input.length - srcCursor
    System.arraycopy(input, srcCursor, result,
      srcCursor + offset, copyLen)
    result
  }

  private def netLengthChange(ops: List[PatchOp]): Int = {
    var offset = 0
    for (op <- ops) {
      op match {
        case PatchInsert(start, text) =>
          offset += text.length
        case PatchReplace(start, end, text) =>
          offset += text.length - (end - start)
        case PatchDelete(start, end) =>
          offset -= end - start
      }
    }
    offset
  }
}
