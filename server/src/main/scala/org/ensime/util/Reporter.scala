package org.ensime.util

import org.slf4j.LoggerFactory

import scala.reflect.internal.util.Position
import scala.tools.nsc.reporters.Reporter

trait ReportHandler {
  def messageUser(str: String): Unit
  def clearAllScalaNotes(): Unit
  def reportScalaNotes(notes: List[Note]): Unit
}

class PresentationReporter(handler: ReportHandler) extends Reporter {

  val log = LoggerFactory.getLogger(classOf[PresentationReporter])
  private var enabled = true
  def enable(): Unit = { enabled = true }
  def disable(): Unit = { enabled = false }

  override def reset(): Unit = {
    super.reset()
    if (enabled) {
      handler.clearAllScalaNotes()
    }
  }

  override def info0(pos: Position, msg: String, severity: Severity, force: Boolean): Unit = {
    severity.count += 1
    try {
      if (severity.id == 0) {
        log.info(msg)
      } else {
        if (enabled) {
          if (pos.isDefined) {
            val source = pos.source
            val f = source.file.absolute.path
            val note = new Note(
              f,
              formatMessage(msg),
              NoteSeverity(severity.id),
              pos.start,
              pos.end,
              pos.line,
              pos.column
            )
            handler.reportScalaNotes(List(note))
          }
        }
      }
    } catch {
      case ex: UnsupportedOperationException =>
        log.warn("Unsupported operation during reporting", ex)
    }
  }

  def formatMessage(msg: String): String = {
    augmentString(msg).map {
      case '\n' | '\r' => ' '
      case c => c
    }
  }
}

