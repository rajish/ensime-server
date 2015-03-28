package org.ensime.server.protocol.swank

import java.io.File

import org.ensime.model._
import org.ensime.core._
import org.ensime.util._
import pimpathon.file._

import UnitTestUtils._

object SwankTestData {

  val typeInfo = new BasicTypeInfo("type1", 7, 'type1, "FOO.type1", List(), List(), None, Some(8))
  val typeInfoStr = """(:arrow-type nil :name "type1" :type-id 7 :decl-as type1 :full-name "FOO.type1" :type-args nil :members nil :pos nil :outer-type-id 8)"""

  val interfaceInfo = new InterfaceInfo(typeInfo, Some("DEF"))
  val typeInspectInfo = new TypeInspectInfo(typeInfo, Some(1), List(interfaceInfo))
  val typeInspectInfoStr = s"""(:type $typeInfoStr :companion-id 1 :interfaces ((:type """ + typeInfoStr + """ :via-view "DEF")) :info-type typeInspect)"""

  val paramSectionInfo = new ParamSectionInfo(List(("ABC", typeInfo)), false)
  val callCompletionInfo = new CallCompletionInfo(typeInfo, List(paramSectionInfo))
  val callCompletionInfoStr = """(:result-type """ + typeInfoStr + """ :param-sections ((:params (("ABC" """ + typeInfoStr + """)) :is-implicit nil)))"""

  val symbolDesignations = SymbolDesignations("/abc",
    List(SymbolDesignation(7, 9, ObjectSymbol),
      SymbolDesignation(11, 22, TraitSymbol)))
  val symbolDesignationsStr = """(:file "/abc" :syms ((object 7 9) (trait 11 22)))"""

  val symbolInfo = new SymbolInfo("name", "localName", None, typeInfo, false, Some(2))
  val symbolInfoStr = """(:name "name" :local-name "localName" :decl-pos nil :type """ + typeInfoStr + """ :is-callable nil :owner-type-id 2)"""

  val batchSourceFile = "/abc"
  val batchSourceFile_str = stringToWireString(batchSourceFile)

  val rangePos1 = new ERangePosition(batchSourceFile, 75, 70, 90)
  val rangePos1Str = """(:file """ + batchSourceFile_str + """ :offset 75 :start 70 :end 90)"""

  val rangePos2 = new ERangePosition(batchSourceFile, 85, 80, 100)
  val rangePos2Str = """(:file """ + batchSourceFile_str + """ :offset 85 :start 80 :end 100)"""

  val packageInfo = new PackageInfo("name", "fullName", List())
  val packageInfoStr = """(:info-type package :name "name" :full-name "fullName" :members nil)"""

  val completionInfo = new CompletionInfo("name", new CompletionSignature(List(List(("abc", "def"), ("hij", "lmn"))), "ABC"), 88, false, 90, Some("BAZ"))
  val completionInfoStr = """(:name "name" :type-sig (((("abc" "def") ("hij" "lmn"))) "ABC") :type-id 88 :is-callable nil :relevance 90 :to-insert "BAZ")"""

  val completionInfo2 = new CompletionInfo("name2", new CompletionSignature(List(List(("abc", "def"))), "ABC"), 90, true, 91, None)
  val completionInfo2Str = """(:name "name2" :type-sig (((("abc" "def"))) "ABC") :type-id 90 :is-callable t :relevance 91 :to-insert nil)"""

  val completionInfoList = List(completionInfo, completionInfo2)
  val completionInfoListStr = "(" + completionInfoStr + " " + completionInfo2Str + ")"

  val refactorFailure = RefactorFailure(7, "message")
  val refactorFailureStr = """(:procedure-id 7 :reason "message" :status failure)"""

  val file1 = CanonFile("/abc/def").file
  val file1_str = fileToWireString(file1)
  val file2 = CanonFile("/test/test/").file
  val file2_str = fileToWireString(file2)
  val file3 = CanonFile("/foo/abc").file
  val file3_str = fileToWireString(file3)
  val file4 = CanonFile("/foo/def").file
  val file4_str = fileToWireString(file4)
  val file5 = CanonFile("/foo/hij").file
  val file5_str = fileToWireString(file5)

  val refactorEffect = new RefactorEffect(9, 'add, List(TextEdit(file3, 5, 7, "aaa")))
  val refactorEffectStr = """(:procedure-id 9 :refactor-type add :status success :changes ((:file """ + file3_str + """ :text "aaa" :from 5 :to 7)))"""

  val refactorResult = new RefactorResult(7, 'abc, List(file3, file1))
  val refactorResultStr = s"""(:procedure-id 7 :refactor-type abc :touched-files ($file3_str $file1_str) :status success)"""

  val sourcePos1 = new LineSourcePosition(file1, 57)
  val sourcePos2 = new LineSourcePosition(file1, 59)
  val sourcePos3 = new EmptySourcePosition()
  val sourcePos4 = new OffsetSourcePosition(file1, 456)

  val breakPoint1 = new Breakpoint(sourcePos1.file, sourcePos1.line)
  val breakPoint2 = new Breakpoint(sourcePos2.file, sourcePos2.line)

  val breakpointList = BreakpointList(List(breakPoint1), List(breakPoint2))
  val breakpointListStr = """(:active ((:file """ + file1_str + """ :line 57)) :pending ((:file """ + file1_str + """ :line 59)))"""

  val debugStackLocal1 = DebugStackLocal(3, "name1", "summary1", "type1")
  val debugStackLocal2 = DebugStackLocal(4, "name2", "summary2", "type2")

  val debugStackFrame = DebugStackFrame(7, List(debugStackLocal1, debugStackLocal2), 4, "class1", "method1", sourcePos1, DebugObjectId(7))

  val debugBacktrace = DebugBacktrace(List(debugStackFrame), "17", "thread1")
  val debugBacktraceStr = s"""(:frames ((:index 7 :locals ((:index 3 :name "name1" :summary "summary1" :type-name "type1") (:index 4 :name "name2" :summary "summary2" :type-name "type2")) :num-args 4 :class-name "class1" :method-name "method1" :pc-location (:file $file1_str :line 57) :this-object-id "7")) :thread-id "17" :thread-name "thread1")"""

  val undoResult = UndoResult(7, List(file3, file4))
  val undoResultStr = """(:id 7 :touched-files (""" + file3_str + """ """ + file4_str + """))"""

  val replConfig = new ReplConfig(Set(file1))
  val replConfigStr = """(:classpath (""" + file1_str + """))"""

  val analyzerFile = new File("Analyzer.scala")
  val fooFile = new File("Foo.scala")

  val abd = CanonFile("abd").file
  val abd_str = fileToWireString(abd)

  val methodSearchRes = MethodSearchResult("abc", "a", 'abcd, Some(LineSourcePosition(file("abd"), 10)), "ownerStr")
  val typeSearchRes = TypeSearchResult("abc", "a", 'abcd, Some(LineSourcePosition(file("abd"), 10)))

  val importSuggestions = new ImportSuggestions(List(List(methodSearchRes, typeSearchRes)))
  val importSuggestionsStr = s"""(((:type method :name "abc" :local-name "a" :decl-as abcd :pos (:type line :file $abd_str :line 10) :owner-name "ownerStr") (:type type :name "abc" :local-name "a" :decl-as abcd :pos (:type line :file $abd_str :line 10))))"""

  val symbolSearchResults = new SymbolSearchResults(List(methodSearchRes, typeSearchRes))
  val symbolSearchResultsStr = s"""((:type method :name "abc" :local-name "a" :decl-as abcd :pos (:type line :file $abd_str :line 10) :owner-name "ownerStr") (:type type :name "abc" :local-name "a" :decl-as abcd :pos (:type line :file $abd_str :line 10)))"""

  val completionInfoCList = CompletionInfoList("fooBar", List(completionInfo))
  val completionInfoCListStr = s"""(:prefix "fooBar" :completions ($completionInfoStr))"""

  val refactorRenameEffect = new RefactorEffect(7, 'rename, List(TextEdit(file3, 5, 7, "aaa")))
  val refactorRenameEffectStr = s"""(:procedure-id 7 :refactor-type rename :changes ((:type edit :file $file3_str :from 5 :to 7 :text "aaa")) :status success)"""

  val fileRange = FileRange("/abc", 7, 9)
  val fileRangeStr = """(:file "/abc" :start 7 :end 9)"""

  val debugLocObjectRef: DebugLocation = DebugObjectReference(57L)
  val debugLocObjectRefStr = """(:type reference :object-id "57")"""

  val debugNullValue = DebugNullValue("typeNameStr")
  val debugNullValueStr = """(:val-type null :type-name "typeNameStr")"""

  val debugArrayInstValue = DebugArrayInstance(3, "typeName", "elementType", DebugObjectId(5L))
  val debugArrayInstValueStr = """(:val-type arr :length 3 :type-name "typeName" :element-type-name "elementType" :object-id "5")"""

  val debugPrimitiveValue = DebugPrimitiveValue("summaryStr", "typeNameStr")
  val debugPrimitiveValueStr = """(:val-type prim :summary "summaryStr" :type-name "typeNameStr")"""

  val debugClassField = DebugClassField(19, "nameStr", "typeNameStr", "summaryStr")
  val debugClassFieldStr = """(:index 19 :name "nameStr" :type-name "typeNameStr" :summary "summaryStr")"""

  val debugStringValue = DebugStringInstance("summaryStr", List(debugClassField), "typeNameStr", DebugObjectId(6L))
  val debugStringValueStr = s"""(:val-type str :summary "summaryStr" :fields ($debugClassFieldStr) :type-name "typeNameStr" :object-id "6")"""

  val note1 = new Note("file1", "note1", NoteError, 23, 33, 19, 8)
  val note1Str = """(:file "file1" :msg "note1" :severity error :beg 23 :end 33 :line 19 :col 8)"""
  val note2 = new Note("file1", "note2", NoteWarn, 23, 33, 19, 8)
  val note2Str = """( :file "file1" :msg "note2" :severity warn :beg 23 :end 33 :line 19 :col 8)"""

  val noteList = NewScalaNotesEvent(isFull = true, List(note1, note2))
  val noteListStr = "(:is-full t :notes (" + note1Str + " " + note2Str + "))"

  val entityInfo: TypeInfo = new ArrowTypeInfo("Arrow1", 8, typeInfo, List(paramSectionInfo))
  val entityInfoStr = """(:arrow-type t :name "Arrow1" :type-id 8 :result-type (:arrow-type nil :name "type1" :type-id 7 :decl-as type1 :full-name "FOO.type1" :type-args nil :members nil :pos nil :outer-type-id 8) :param-sections ((:params (("ABC" (:arrow-type nil :name "type1" :type-id 7 :decl-as type1 :full-name "FOO.type1" :type-args nil :members nil :pos nil :outer-type-id 8))) :is-implicit nil)))"""
}
