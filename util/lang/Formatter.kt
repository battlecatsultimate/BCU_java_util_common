package common.util.langimport

import common.pack.PackData
import common.util.lang.Formatter
import common.util.pack.Background
import java.util.*

com.google.api.client.json.jackson2.JacksonFactoryimport com.google.api.services.drive.DriveScopesimport com.google.api.client.util.store.FileDataStoreFactoryimport com.google.api.client.http.HttpTransportimport com.google.api.services.drive.Driveimport kotlin.Throwsimport java.io.IOExceptionimport io.drive.DriveUtilimport java.io.FileNotFoundExceptionimport java.io.FileInputStreamimport com.google.api.client.googleapis.auth.oauth2.GoogleClientSecretsimport java.io.InputStreamReaderimport com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlowimport com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledAppimport com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiverimport com.google.api.client.googleapis.javanet.GoogleNetHttpTransportimport kotlin.jvm.JvmStaticimport io.drive.DrvieInitimport com.google.api.client.http.javanet.NetHttpTransportimport com.google.api.services.drive.model.FileListimport java.io.BufferedInputStreamimport java.io.FileOutputStreamimport com.google.api.client.googleapis.media.MediaHttpDownloaderimport io.WebFileIOimport io.BCJSONimport page.LoadPageimport org.json.JSONObjectimport org.json.JSONArrayimport main.MainBCUimport main.Optsimport common.CommonStaticimport java.util.TreeMapimport java.util.Arraysimport java.io.BufferedReaderimport io.BCMusicimport common.util.stage.Musicimport io.BCPlayerimport java.util.HashMapimport javax.sound.sampled.Clipimport java.io.ByteArrayInputStreamimport javax.sound.sampled.AudioInputStreamimport javax.sound.sampled.AudioSystemimport javax.sound.sampled.DataLineimport javax.sound.sampled.FloatControlimport javax.sound.sampled.LineEventimport com.google.api.client.googleapis.media.MediaHttpDownloaderProgressListenerimport com.google.api.client.googleapis.media.MediaHttpDownloader.DownloadStateimport common.io.DataIOimport io.BCUReaderimport common.io.InStreamimport com.google.gson.JsonElementimport common.io.json.JsonDecoderimport com.google.gson.JsonObjectimport page.MainFrameimport page.view.ViewBox.Confimport page.MainLocaleimport page.battle.BattleInfoPageimport page.support.Exporterimport page.support.Importerimport common.pack.Context.ErrTypeimport common.util.stage.MapColcimport common.util.stage.MapColc.DefMapColcimport common.util.lang.MultiLangContimport common.util.stage.StageMapimport common.util.unit.Enemyimport io.BCUWriterimport java.text.SimpleDateFormatimport java.io.PrintStreamimport common.io.OutStreamimport common.battle.BasisSetimport res.AnimatedGifEncoderimport java.awt.image.BufferedImageimport javax.imageio.ImageIOimport java.security.MessageDigestimport java.security.NoSuchAlgorithmExceptionimport common.io.json.JsonEncoderimport java.io.FileWriterimport com.google.api.client.http.GenericUrlimport org.apache.http.impl .client.CloseableHttpClientimport org.apache.http.impl .client.HttpClientsimport org.apache.http.client.methods.HttpPostimport org.apache.http.entity.mime.content.FileBodyimport org.apache.http.entity.mime.MultipartEntityBuilderimport org.apache.http.entity.mime.HttpMultipartModeimport org.apache.http.HttpEntityimport org.apache.http.util.EntityUtilsimport com.google.api.client.http.HttpRequestInitializerimport com.google.api.client.http.HttpBackOffUnsuccessfulResponseHandlerimport com.google.api.client.util.ExponentialBackOffimport com.google.api.client.http.HttpBackOffIOExceptionHandlerimport res.NeuQuantimport res.LZWEncoderimport java.io.BufferedOutputStreamimport java.awt.Graphics2Dimport java.awt.image.DataBufferByteimport common.system.fake.FakeImageimport utilpc.awt.FIBIimport jogl.util.AmbImageimport common.system.files.VFileimport jogl.util.GLImageimport com.jogamp.opengl.util.texture.TextureDataimport common.system.Pimport com.jogamp.opengl.util.texture.TextureIOimport jogl.GLStaticimport com.jogamp.opengl.util.texture.awt.AWTTextureIOimport java.awt.AlphaCompositeimport common.system.fake.FakeImage.Markerimport jogl.util.GLGraphicsimport com.jogamp.opengl.GL2import jogl.util.GeoAutoimport com.jogamp.opengl.GL2ES3import com.jogamp.opengl.GLimport common.system.fake.FakeGraphicsimport common.system.fake.FakeTransformimport jogl.util.ResManagerimport jogl.util.GLGraphics.GeomGimport jogl.util.GLGraphics.GLCimport jogl.util.GLGraphics.GLTimport com.jogamp.opengl.GL2ES2import com.jogamp.opengl.util.glsl.ShaderCodeimport com.jogamp.opengl.util.glsl.ShaderProgramimport com.jogamp.opengl.GLExceptionimport jogl.StdGLCimport jogl.Tempimport common.util.anim.AnimUimport common.util.anim.EAnimUimport jogl.util.GLIBimport javax.swing.JFrameimport common.util.anim.AnimCEimport common.util.anim.AnimU.UTypeimport com.jogamp.opengl.util.FPSAnimatorimport com.jogamp.opengl.GLEventListenerimport com.jogamp.opengl.GLAutoDrawableimport page.awt.BBBuilderimport page.battle.BattleBox.OuterBoximport common.battle.SBCtrlimport page.battle.BattleBoximport jogl.GLBattleBoximport common.battle.BattleFieldimport page.anim.IconBoximport jogl.GLIconBoximport jogl.GLBBRecdimport page.awt.RecdThreadimport page.view.ViewBoximport jogl.GLViewBoximport page.view.ViewBox.Controllerimport java.awt.AWTExceptionimport page.battle.BBRecdimport jogl.GLRecorderimport com.jogamp.opengl.GLProfileimport com.jogamp.opengl.GLCapabilitiesimport page.anim.IconBox.IBCtrlimport page.anim.IconBox.IBConfimport page.view.ViewBox.VBExporterimport jogl.GLRecdBImgimport page.JTGimport jogl.GLCstdimport jogl.GLVBExporterimport common.util.anim.EAnimIimport page.RetFuncimport page.battle.BattleBox.BBPainterimport page.battle.BBCtrlimport javax.swing.JOptionPaneimport kotlin.jvm.Strictfpimport main.Invimport javax.swing.SwingUtilitiesimport java.lang.InterruptedExceptionimport utilpc.UtilPC.PCItrimport utilpc.awt.PCIBimport jogl.GLBBBimport page.awt.AWTBBBimport utilpc.Themeimport page.MainPageimport common.io.assets.AssetLoaderimport common.pack.Source.Workspaceimport common.io.PackLoader.ZipDesc.FileDescimport common.io.assets.Adminimport page.awt.BattleBoxDefimport page.awt.IconBoxDefimport page.awt.BBRecdAWTimport page.awt.ViewBoxDefimport org.jcodec.api.awt.AWTSequenceEncoderimport page.awt.RecdThread.PNGThreadimport page.awt.RecdThread.MP4Threadimport page.awt.RecdThread.GIFThreadimport java.awt.GradientPaintimport utilpc.awt.FG2Dimport page.anim.TreeContimport javax.swing.JTreeimport javax.swing.event.TreeExpansionListenerimport common.util.anim.MaModelimport javax.swing.tree.DefaultMutableTreeNodeimport javax.swing.event.TreeExpansionEventimport java.util.function.IntPredicateimport javax.swing.tree.DefaultTreeModelimport common.util.anim.EAnimDimport page.anim.AnimBoximport utilpc.PPimport common.CommonStatic.BCAuxAssetsimport common.CommonStatic.EditLinkimport page.JBTNimport page.anim.DIYViewPageimport page.anim.ImgCutEditPageimport page.anim.MaModelEditPageimport page.anim.MaAnimEditPageimport page.anim.EditHeadimport java.awt.event.ActionListenerimport page.anim.AbEditPageimport common.util.anim.EAnimSimport page.anim.ModelBoximport common.util.anim.ImgCutimport page.view.AbViewPageimport javax.swing.JListimport javax.swing.JScrollPaneimport javax.swing.JComboBoximport utilpc.UtilPCimport javax.swing.event.ListSelectionListenerimport javax.swing.event.ListSelectionEventimport common.system.VImgimport page.support.AnimLCRimport page.support.AnimTableimport common.util.anim.MaAnimimport java.util.EventObjectimport javax.swing.text.JTextComponentimport page.anim.PartEditTableimport javax.swing.ListSelectionModelimport page.support.AnimTableTHimport page.JTFimport utilpc.ReColorimport page.anim.ImgCutEditTableimport page.anim.SpriteBoximport page.anim.SpriteEditPageimport java.awt.event.FocusAdapterimport java.awt.event.FocusEventimport common.pack.PackData.UserPackimport utilpc.Algorithm.SRResultimport page.anim.MaAnimEditTableimport javax.swing.JSliderimport java.awt.event.MouseWheelEventimport common.util.anim.EPartimport javax.swing.event.ChangeEventimport page.anim.AdvAnimEditPageimport javax.swing.BorderFactoryimport page.JLimport javax.swing.ImageIconimport page.anim.MMTreeimport javax.swing.event.TreeSelectionListenerimport javax.swing.event.TreeSelectionEventimport page.support.AbJTableimport page.anim.MaModelEditTableimport page.info.edit.ProcTableimport page.info.edit.ProcTable.AtkProcTableimport page.info.edit.SwingEditorimport page.info.edit.ProcTable.MainProcTableimport page.support.ListJtfPolicyimport page.info.edit.SwingEditor.SwingEGimport common.util.Data.Procimport java.lang.Runnableimport javax.swing.JComponentimport page.info.edit.LimitTableimport page.pack.CharaGroupPageimport page.pack.LvRestrictPageimport javax.swing.SwingConstantsimport common.util.lang.Editors.EditorGroupimport common.util.lang.Editors.EdiFieldimport common.util.lang.Editorsimport common.util.lang.ProcLangimport page.info.edit.EntityEditPageimport common.util.lang.Editors.EditorSupplierimport common.util.lang.Editors.EditControlimport page.info.edit.SwingEditor.IntEditorimport page.info.edit.SwingEditor.BoolEditorimport page.info.edit.SwingEditor.IdEditorimport page.SupPageimport common.util.unit.AbEnemyimport common.pack.IndexContainer.Indexableimport common.pack.Context.SupExcimport common.battle.data .AtkDataModelimport utilpc.Interpretimport common.battle.data .CustomEntityimport page.info.filter.UnitEditBoximport common.battle.data .CustomUnitimport common.util.stage.SCGroupimport page.info.edit.SCGroupEditTableimport common.util.stage.SCDefimport page.info.filter.EnemyEditBoximport common.battle.data .CustomEnemyimport page.info.StageFilterPageimport page.view.BGViewPageimport page.view.CastleViewPageimport page.view.MusicPageimport common.util.stage.CastleImgimport common.util.stage.CastleListimport java.text.DecimalFormatimport common.util.stage.Recdimport common.util.stage.MapColc.PackMapColcimport page.info.edit.StageEditTableimport page.support.ReorderListimport page.info.edit.HeadEditTableimport page.info.filter.EnemyFindPageimport page.battle.BattleSetupPageimport page.info.edit.AdvStEditPageimport page.battle.StRecdPageimport page.info.edit.LimitEditPageimport page.support.ReorderListenerimport common.util.pack.Soulimport page.info.edit.AtkEditTableimport page.info.filter.UnitFindPageimport common.battle.Basisimport common.util.Data.Proc.IMUimport javax.swing.DefaultComboBoxModelimport common.util.Animableimport common.util.pack.Soul.SoulTypeimport page.view.UnitViewPageimport page.view.EnemyViewPageimport page.info.edit.SwingEditor.EditCtrlimport page.support.Reorderableimport page.info.EnemyInfoPageimport common.util.unit.EneRandimport page.pack.EREditPageimport page.support.InTableTHimport page.support.EnemyTCRimport javax.swing.DefaultListCellRendererimport page.info.filter.UnitListTableimport page.info.filter.UnitFilterBoximport page.info.filter.EnemyListTableimport page.info.filter.EnemyFilterBoximport page.info.filter.UFBButtonimport page.info.filter.UFBListimport common.battle.data .MaskUnitimport javax.swing.AbstractButtonimport page.support.SortTableimport page.info.UnitInfoPageimport page.support.UnitTCRimport page.info.filter.EFBButtonimport page.info.filter.EFBListimport common.util.stage.LvRestrictimport common.util.stage.CharaGroupimport page.info.StageTableimport page.info.TreaTableimport javax.swing.JPanelimport page.info.UnitInfoTableimport page.basis.BasisPageimport kotlin.jvm.JvmOverloadsimport page.info.EnemyInfoTableimport common.util.stage.RandStageimport page.info.StagePageimport page.info.StageRandPageimport common.util.unit.EFormimport page.pack.EREditTableimport common.util.EREntimport common.pack.FixIndexListimport page.support.UnitLCRimport page.pack.RecdPackPageimport page.pack.CastleEditPageimport page.pack.BGEditPageimport page.pack.CGLREditPageimport common.pack.Source.ZipSourceimport page.info.edit.EnemyEditPageimport page.info.edit.StageEditPageimport page.info.StageViewPageimport page.pack.UnitManagePageimport page.pack.MusicEditPageimport page.battle.AbRecdPageimport common.system.files.VFileRootimport java.awt.Desktopimport common.pack.PackDataimport common.util.unit.UnitLevelimport page.info.edit.FormEditPageimport common.util.anim.AnimIimport common.util.anim.AnimI.AnimTypeimport common.util.anim.AnimDimport common.battle.data .Orbimport page.basis.LineUpBoximport page.basis.LubContimport common.battle.BasisLUimport page.basis.ComboListTableimport page.basis.ComboListimport page.basis.NyCasBoximport page.basis.UnitFLUPageimport common.util.unit.Comboimport page.basis.LevelEditPageimport common.util.pack.NyCastleimport common.battle.LineUpimport common.system.SymCoordimport java.util.TreeSetimport page.basis.OrbBoximport javax.swing.table.DefaultTableCellRendererimport javax.swing.JTableimport common.CommonStatic.BattleConstimport common.battle.StageBasisimport common.util.ImgCoreimport common.battle.attack.ContAbimport common.battle.entity.EAnimContimport common.battle.entity.WaprContimport page.battle.RecdManagePageimport page.battle.ComingTableimport common.util.stage.EStageimport page.battle.EntityTableimport common.battle.data .MaskEnemyimport common.battle.SBRplyimport common.battle.entity.AbEntityimport page.battle.RecdSavePageimport page.LocCompimport page.LocSubCompimport javax.swing.table.TableModelimport page.support.TModelimport javax.swing.event.TableModelListenerimport javax.swing.table.DefaultTableColumnModelimport javax.swing.JFileChooserimport javax.swing.filechooser.FileNameExtensionFilterimport javax.swing.TransferHandlerimport java.awt.datatransfer.Transferableimport java.awt.datatransfer.DataFlavorimport javax.swing.DropModeimport javax.swing.TransferHandler.TransferSupportimport java.awt.dnd.DragSourceimport java.awt.datatransfer.UnsupportedFlavorExceptionimport common.system.Copableimport page.support.AnimTransferimport javax.swing.DefaultListModelimport page.support.InListTHimport java.awt.FocusTraversalPolicyimport javax.swing.JTextFieldimport page.CustomCompimport javax.swing.JToggleButtonimport javax.swing.JButtonimport javax.swing.ToolTipManagerimport javax.swing.JRootPaneimport javax.swing.JProgressBarimport page.ConfigPageimport page.view.EffectViewPageimport page.pack.PackEditPageimport page.pack.ResourcePageimport javax.swing.WindowConstantsimport java.awt.event.AWTEventListenerimport java.awt.AWTEventimport java.awt.event.ComponentAdapterimport java.awt.event.ComponentEventimport java.util.ConcurrentModificationExceptionimport javax.swing.plaf.FontUIResourceimport java.util.Enumerationimport javax.swing.UIManagerimport common.CommonStatic.FakeKeyimport page.LocSubComp.LocBinderimport page.LSCPopimport java.awt.BorderLayoutimport java.awt.GridLayoutimport javax.swing.JTextPaneimport page.TTTimport java.util.ResourceBundleimport java.util.MissingResourceExceptionimport java.util.Localeimport common.io.json.Test.JsonTest_2import common.pack.PackData.PackDescimport common.io.PackLoaderimport common.io.PackLoader.Preloadimport common.io.PackLoader.ZipDescimport common.io.json.Testimport common.io.json.JsonClassimport common.io.json.JsonFieldimport common.io.json.JsonField.GenTypeimport common.io.json.Test.JsonTest_0.JsonDimport common.io.json.JsonClass.RTypeimport java.util.HashSetimport common.io.json.JsonDecoder.OnInjectedimport common.io.json.JsonField.IOTypeimport common.io.json.JsonExceptionimport common.io.json.JsonClass.NoTagimport common.io.json.JsonField.SerTypeimport common.io.json.JsonClass.WTypeimport kotlin.reflect.KClassimport com.google.gson.JsonArrayimport common.io.assets.Admin.StaticPermittedimport common.io.json.JsonClass.JCGenericimport common.io.json.JsonClass.JCGetterimport com.google.gson.JsonPrimitiveimport com.google.gson.JsonNullimport common.io.json.JsonClass.JCIdentifierimport java.lang.ClassNotFoundExceptionimport common.io.assets.AssetLoader.AssetHeaderimport common.io.assets.AssetLoader.AssetHeader.AssetEntryimport common.io.InStreamDefimport common.io.BCUExceptionimport java.io.UnsupportedEncodingExceptionimport common.io.OutStreamDefimport javax.crypto.Cipherimport javax.crypto.spec.IvParameterSpecimport javax.crypto.spec.SecretKeySpecimport common.io.PackLoader.FileSaverimport common.system.files.FDByteimport common.io.json.JsonClass.JCConstructorimport common.io.PackLoader.FileLoader.FLStreamimport common.io.PackLoader.PatchFileimport java.lang.NullPointerExceptionimport java.lang.IndexOutOfBoundsExceptionimport common.io.MultiStreamimport java.io.RandomAccessFileimport common.io.MultiStream.TrueStreamimport java.lang.RuntimeExceptionimport common.pack.Source.ResourceLocationimport common.pack.Source.AnimLoaderimport common.pack.Source.SourceAnimLoaderimport common.util.anim.AnimCIimport common.system.files.FDFileimport common.pack.IndexContainerimport common.battle.data .PCoinimport common.util.pack.EffAnimimport common.battle.data .DataEnemyimport common.util.stage.Limit.DefLimitimport common.pack.IndexContainer.Reductorimport common.pack.FixIndexList.FixIndexMapimport common.pack.VerFixer.IdFixerimport common.pack.IndexContainer.IndexContimport common.pack.IndexContainer.ContGetterimport common.util.stage.CastleList.PackCasListimport common.util.Data.Proc.THEMEimport common.CommonStatic.ImgReaderimport common.pack.VerFixerimport common.pack.VerFixer.VerFixerExceptionimport java.lang.NumberFormatExceptionimport common.pack.Source.SourceAnimSaverimport common.pack.VerFixer.EnemyFixerimport common.pack.VerFixer.PackFixerimport common.pack.PackData.DefPackimport java.util.function.BiConsumerimport common.util.BattleStaticimport common.util.anim.AnimU.ImageKeeperimport common.util.anim.AnimCE.AnimCELoaderimport common.util.anim.AnimCI.AnimCIKeeperimport common.util.anim.AnimUD.DefImgLoaderimport common.util.BattleObjimport common.util.Data.Proc.ProcItemimport common.util.lang.ProcLang.ItemLangimport common.util.lang.LocaleCenter.Displayableimport common.util.lang.Editors.DispItemimport common.util.lang.LocaleCenter.ObjBinderimport common.util.lang.LocaleCenter.ObjBinder.BinderFuncimport common.util.Data.Proc.PROBimport org.jcodec.common.tools.MathUtilimport common.util.Data.Proc.PTimport common.util.Data.Proc.PTDimport common.util.Data.Proc.PMimport common.util.Data.Proc.WAVEimport common.util.Data.Proc.WEAKimport common.util.Data.Proc.STRONGimport common.util.Data.Proc.BURROWimport common.util.Data.Proc.REVIVEimport common.util.Data.Proc.SUMMONimport common.util.Data.Proc.MOVEWAVEimport common.util.Data.Proc.POISONimport common.util.Data.Proc.CRITIimport common.util.Data.Proc.VOLCimport common.util.Data.Proc.ARMORimport common.util.Data.Proc.SPEEDimport java.util.LinkedHashMapimport common.util.lang.LocaleCenter.DisplayItemimport common.util.lang.ProcLang.ProcLangStoreimport common.util.lang.Formatter.IntExpimport common.util.lang.Formatter.RefObjimport common.util.lang.Formatter.BoolExpimport common.util.lang.Formatter.BoolElemimport common.util.lang.Formatter.IElemimport common.util.lang.Formatter.Contimport common.util.lang.Formatter.Elemimport common.util.lang.Formatter.RefElemimport common.util.lang.Formatter.RefFieldimport common.util.lang.Formatter.RefFuncimport common.util.lang.Formatter.TextRefimport common.util.lang.Formatter.CodeBlockimport common.util.lang.Formatter.TextPlainimport common.util.unit.Unit.UnitInfoimport common.util.lang.MultiLangCont.MultiLangStaticsimport common.util.pack.EffAnim.EffTypeimport common.util.pack.EffAnim.ArmorEffimport common.util.pack.EffAnim.BarEneEffimport common.util.pack.EffAnim.BarrierEffimport common.util.pack.EffAnim.DefEffimport common.util.pack.EffAnim.WarpEffimport common.util.pack.EffAnim.ZombieEffimport common.util.pack.EffAnim.KBEffimport common.util.pack.EffAnim.SniperEffimport common.util.pack.EffAnim.VolcEffimport common.util.pack.EffAnim.SpeedEffimport common.util.pack.EffAnim.WeakUpEffimport common.util.pack.EffAnim.EffAnimStoreimport common.util.pack.NyCastle.NyTypeimport common.util.pack.WaveAnimimport common.util.pack.WaveAnim.WaveTypeimport common.util.pack.Background.BGWvTypeimport common.util.unit.Form.FormJsonimport common.system.BasedCopableimport common.util.anim.AnimUDimport common.battle.data .DataUnitimport common.battle.entity.EUnitimport common.battle.entity.EEnemyimport common.util.EntRandimport common.util.stage.Recd.Waitimport java.lang.CloneNotSupportedExceptionimport common.util.stage.StageMap.StageMapInfoimport common.util.stage.Stage.StageInfoimport common.util.stage.Limit.PackLimitimport common.util.stage.MapColc.ClipMapColcimport common.util.stage.CastleList.DefCasListimport common.util.stage.MapColc.StItrimport common.util.Data.Proc.IntType.BitCountimport common.util.CopRandimport common.util.LockGLimport java.lang.IllegalAccessExceptionimport common.battle.data .MaskAtkimport common.battle.data .DefaultDataimport common.battle.data .DataAtkimport common.battle.data .MaskEntityimport common.battle.data .DataEntityimport common.battle.attack.AtkModelAbimport common.battle.attack.AttackAbimport common.battle.attack.AttackSimpleimport common.battle.attack.AttackWaveimport common.battle.entity.Cannonimport common.battle.attack.AttackVolcanoimport common.battle.attack.ContWaveAbimport common.battle.attack.ContWaveDefimport common.battle.attack.AtkModelEntityimport common.battle.entity.EntContimport common.battle.attack.ContMoveimport common.battle.attack.ContVolcanoimport common.battle.attack.ContWaveCanonimport common.battle.attack.AtkModelEnemyimport common.battle.attack.AtkModelUnitimport common.battle.attack.AttackCanonimport common.battle.entity.EUnit.OrbHandlerimport common.battle.entity.Entity.AnimManagerimport common.battle.entity.Entity.AtkManagerimport common.battle.entity.Entity.ZombXimport common.battle.entity.Entity.KBManagerimport common.battle.entity.Entity.PoisonTokenimport common.battle.entity.Entity.WeakTokenimport common.battle.Treasureimport common.battle.MirrorSetimport common.battle.Releaseimport common.battle.ELineUpimport common.battle.entity.Sniperimport common.battle.entity.ECastleimport java.util.Dequeimport common.CommonStatic.Itfimport java.lang.Characterimport common.CommonStatic.ImgWriterimport utilpc.awt.FTATimport utilpc.awt.Blenderimport java.awt.RenderingHintsimport utilpc.awt.BIBuilderimport java.awt.CompositeContextimport java.awt.image.Rasterimport java.awt.image.WritableRasterimport utilpc.ColorSetimport utilpc.OggTimeReaderimport utilpc.UtilPC.PCItr.MusicReaderimport utilpc.UtilPC.PCItr.PCALimport javax.swing.UIManager.LookAndFeelInfoimport java.lang.InstantiationExceptionimport javax.swing.UnsupportedLookAndFeelExceptionimport utilpc.Algorithm.ColorShiftimport utilpc.Algorithm.StackRect
class Formatter private constructor(private val str: String, private val obj: Any, private val ctx: Any) {
    @JsonClass
    class Context {
        val left = "("
        val right = ")"
        val sqleft = "["
        val sqright = "]"
        val crleft = "{"
        val crright = "}"

        @JsonField
        var isEnemy = false

        @JsonField
        var useSecond = false

        constructor() {}
        constructor(ene: Boolean, sec: Boolean) {
            isEnemy = ene
            useSecond = sec
        }

        fun abs(v: Int): String {
            return "" + Math.abs(v)
        }

        fun bg(id: PackData.Identifier<Background?>?): String {
            return PackData.Identifier.Companion.get<Background>(id).toString() + ""
        }

        fun dispTime(time: Int): String {
            return if (useSecond) toSecond(time) + "s" else time.toString() + "f"
        }

        fun entity(id: PackData.Identifier<*>?): String {
            return PackData.Identifier.Companion.get(id).toString() + ""
        }

        fun toSecond(time: Int): String {
            return "" + time * 100 / 30 / 100.0
        }
    }

    private inner class BoolElem(start: Int, end: Int) : Formatter.Comp(start, end) {
        @Throws(Exception::class)
        fun eval(): Boolean {
            for (i in Formatter.Companion.MATCH.indices) for (j in p0 until p1 - Formatter.Companion.MATCH.get(i).length + 1) if (test(i, j)) {
                val fi0 = IntExp(p0, j).eval()
                val fi1 = IntExp(j + Formatter.Companion.MATCH.get(i).length, p1).eval()
                if (i == 0) return fi0 >= fi1
                if (i == 1) return fi0 <= fi1
                if (i == 2) return fi0 == fi1
                if (i == 3) return fi0 != fi1
                if (i == 4) return fi0 > fi1
                if (i == 5) return fi0 < fi1
            }
            return RefObj(p0, p1).eval() as Boolean
        }

        private fun test(i: Int, j: Int): Boolean {
            for (k in 0 until Formatter.Companion.MATCH.get(i).length) if (str[j + k] != Formatter.Companion.MATCH.get(i).get(k)) return false
            return true
        }
    }

    private inner class BoolExp(start: Int, end: Int) : Formatter.Comp(start, end) {
        private var ind: Int

        @Throws(Exception::class)
        fun eval(): Boolean {
            val stack = Stack<Boolean>()
            stack.push(nextElem())
            while (ind < p1) {
                val ch = str[ind++]
                if (ch == '&') stack.push(stack.pop() and nextElem()) else if (ch == '|') stack.push(nextElem()) else throw Exception("unknown operator " + ch + " at " + (ind - 1))
            }
            var b = false
            for (bi in stack) b = b or bi
            return b
        }

        @Throws(Exception::class)
        private fun nextElem(): Boolean {
            var ch = str[ind]
            val neg = ch == '!'
            if (neg) ch = str[++ind]
            if (ch == '!') throw Exception("double ! at $ind")
            if (ch == '(') {
                var depth = 1
                val pre = ++ind
                while (depth > 0) {
                    val chr = str[ind++]
                    if (chr == '(') depth++
                    if (chr == ')') depth--
                }
                return neg xor BoolExp(pre, ind - 1).eval()
            }
            val pre = ind
            while (ch != '&' && ch != '|' && ind < p1) ch = str[++ind]
            return neg xor BoolElem(pre, ind).eval()
        }

        init {
            ind = p0
        }
    }

    private inner class Code private constructor(private val cond: BoolExp, private val data: Formatter.Root) : IElem {
        @Throws(Exception::class)
        override fun build(sb: StringBuilder) {
            if (cond.eval()) data.build(sb)
        }
    }

    private inner class CodeBlock(start: Int, end: Int) : Cont(start, end) {
        init {
            var i = p0
            while (i < p1) {
                val ch = str[i++]
                if (ch == '(') {
                    var depth = 1
                    var pre = i
                    while (depth > 0) {
                        if (i >= p1) throw Exception("unfinished at $i")
                        val chr = str[i++]
                        if (chr == '(') depth++
                        if (chr == ')') depth--
                    }
                    val cond = BoolExp(pre, i - 1)
                    while (str[i++] != '{') if (i >= p1) throw Exception("unfinished at $i")
                    depth = 1
                    pre = i
                    while (depth > 0) {
                        if (i >= p1) throw Exception("unfinished at $i")
                        val chr = str[i++]
                        if (chr == '{') depth++
                        if (chr == '}') depth--
                    }
                    val data: Formatter.Root = Formatter.Root(pre, i - 1)
                    list.add(Formatter.Code(cond, data))
                }
            }
        }
    }

    private abstract inner class Comp(val p0: Int, val p1: Int)
    private abstract inner class Cont(start: Int, end: Int) : Elem(start, end) {
        val list: MutableList<IElem> = ArrayList()
        @Throws(Exception::class)
        override fun build(sb: StringBuilder) {
            for (e in list) e.build(sb)
        }
    }

    private abstract inner class Elem(start: Int, end: Int) : Formatter.Comp(start, end), IElem
    private interface IElem {
        @Throws(Exception::class)
        fun build(sb: StringBuilder)
    }

    private inner class IntExp(start: Int, end: Int) : Formatter.Comp(start, end) {
        private var ind: Int

        @Throws(Exception::class)
        fun eval(): Int {
            val stack = Stack<Int>()
            var opera = '\u0000'
            stack.push(nextElem())
            while (ind < p1) {
                val ch = str[ind++]
                if (ch == '*') stack.push(stack.pop() * nextElem()) else if (ch == '/') stack.push(stack.pop() / nextElem()) else if (ch == '%') stack.push(stack.pop() % nextElem()) else if (ch == '+' || ch == '-') {
                    if (opera != ' ') {
                        val b = stack.pop()
                        val a = stack.pop()
                        if (opera == '+') stack.push(a + b) else stack.push(a - b)
                    }
                    stack.push(nextElem())
                    opera = ch
                } else throw Exception("unknown operator " + ch + " at " + (ind - 1))
            }
            if (opera != '\u0000') {
                val b = stack.pop()
                val a = stack.pop()
                if (opera == '+') stack.push(a + b) else stack.push(a - b)
            }
            return stack.pop()
        }

        @Throws(Exception::class)
        private fun nextElem(): Int {
            var ch = str[ind]
            var neg = 1
            if (ch == '-') {
                neg = -1
                ch = str[++ind]
            }
            if (ch == '(') {
                var depth = 1
                val pre = ++ind
                while (depth > 0) {
                    val chr = str[ind++]
                    if (chr == '(') depth++
                    if (chr == ')') depth--
                }
                return neg * IntExp(pre, ind - 1).eval()
            }
            if (ch >= '0' && ch <= '9') return neg * readNumber()
            val pre = ind
            while (ch != '+' && ch != '-' && ch != '*' && ch != '/' && ch != '%' && ind < p1) ch = str[++ind]
            return neg * (RefObj(pre, ind).eval() as Int?)!!
        }

        @Throws(Exception::class)
        private fun readNumber(): Int {
            var ans = 0
            while (ind < p1) {
                val chr = str[ind]
                if (chr < '0' || chr > '9') break
                ind++
                ans = ans * 10 + chr.toInt() - '0'.toInt()
            }
            return ans
        }

        init {
            ind = p0
        }
    }

    private abstract inner class RefElem(start: Int, end: Int) : Formatter.Comp(start, end) {
        @Throws(Exception::class)
        abstract fun eval(parent: Any?): Any?
    }

    private inner class RefField(start: Int, end: Int) : RefElem(start, end) {
        @Throws(Exception::class)
        override fun eval(parent: Any?): Any? {
            var parent = parent
            if (str[p0] == '_' && parent != null) throw Exception("global only allowed for bottom level")
            if (parent == null) parent = if (str[p0] == '_') ctx else obj
            val name = str.substring(if (str[p0] == '_') p0 + 1 else p0, p1)
            val f = parent.javaClass.getField(name)
            return f[parent]
        }
    }

    private inner class RefFunc(start: Int, end: Int) : RefElem(start, end) {
        val list: MutableList<RefObj> = ArrayList()
        @Throws(Exception::class)
        override fun eval(parent: Any?): Any? {
            var parent = parent
            if (str[p0] == '_' && parent != null) throw Exception("global only allowed for bottom level: at $p0")
            if (parent == null) parent = if (str[p0] == '_') ctx else obj
            val name = str.substring(if (str[p0] == '_') p0 + 1 else p0, p1)
            val ms = parent.javaClass.methods
            val args = arrayOfNulls<Any>(list.size)
            for (i in args.indices) args[i] = list[i].eval()
            for (m in ms) if (m.name == name && m.parameterCount == list.size) return m.invoke(parent, *args)
            throw Exception("function " + name + " not found for class " + parent.javaClass)
        }
    }

    private inner class RefObj(start: Int, end: Int) : Elem(start, end) {
        private val list: MutableList<RefElem> = ArrayList()
        @Throws(Exception::class)
        override fun build(sb: StringBuilder) {
            sb.append("" + eval())
        }

        @Throws(Exception::class)
        fun eval(): Any? {
            var obj: Any? = null
            for (e in list) obj = e.eval(obj)
            return obj
        }

        init {
            var pre = p0
            var i = p0
            while (i < p1) {
                val ch = str[i++]
                if (ch == '.') {
                    list.add(RefField(pre, i - 1))
                    pre = i
                }
                if (ch == '(') {
                    val func = RefFunc(pre, i - 1)
                    pre = i
                    var depth = 1
                    while (depth > 0) {
                        if (i >= p1) throw Exception("unfinished at $i")
                        val chr = str[i++]
                        if (chr == '(') depth++
                        if (chr == ')') {
                            depth--
                            if (depth == 0) {
                                if (i - 1 > pre) func.list.add(RefObj(pre, i - 1))
                                pre = i
                            }
                        }
                        if (chr == ',' && depth == 1) {
                            func.list.add(RefObj(pre, i - 1))
                            pre = i
                        }
                    }
                    list.add(func)
                }
            }
            if (pre < p1) list.add(RefField(pre, p1))
        }
    }

    private inner class Root private constructor(start: Int, end: Int) : Cont(start, end) {
        init {
            var pre = p0
            var deepth = 0
            for (i in p0 until p1) {
                val ch = str[i]
                if (ch == '[') {
                    if (deepth == 0 && i > pre) {
                        list.add(TextRef(pre, i))
                        pre = i + 1
                    }
                    deepth++
                }
                if (ch == ']') {
                    deepth--
                    if (deepth == 0 && i > pre) {
                        list.add(CodeBlock(pre, i))
                        pre = i + 1
                    }
                }
            }
            if (pre < p1) list.add(TextRef(pre, p1))
        }
    }

    private inner class TextPlain(start: Int, end: Int) : Elem(start, end) {
        override fun build(sb: StringBuilder) {
            sb.append(str, p0, p1)
        }
    }

    private inner class TextRef(start: Int, end: Int) : Cont(start, end) {
        init {
            var pre = p0
            var depth = 0
            for (i in p0 until p1) {
                val ch = str[i]
                if (ch == '(') {
                    if (depth == 0 && i > pre) list.add(TextPlain(pre, i))
                    if (depth == 0) pre = i + 1
                    depth++
                }
                if (ch == ')') {
                    depth--
                    if (depth == 0 && i > pre) list.add(RefObj(pre, i))
                    if (depth == 0) pre = i + 1
                }
            }
            if (pre < p1) list.add(TextPlain(pre, p1))
        }
    }

    private val root: Formatter.Root
    private fun check(): String? {
        val stack = Stack<Int>()
        for (i in 0 until str.length) {
            val ch = str[i]
            if (ch == '(') stack.push(0)
            if (ch == '[') stack.push(1)
            if (ch == '{') stack.push(2)
            if (ch == ')' && (stack.isEmpty() || stack.pop() != 0)) return "bracket ) unexpected at $i"
            if (ch == ']' && (stack.isEmpty() || stack.pop() != 1)) return "bracket ] unexpected at $i"
            if (ch == '}' && (stack.isEmpty() || stack.pop() != 2)) return "bracket } unexpected at $i"
        }
        return if (stack.isEmpty()) null else "unenclosed bracket: $stack"
    }

    companion object {
        @StaticPermitted
        private val MATCH = arrayOf(">=", "<=", "==", "!=", ">", "<")
        fun format(pattern: String, obj: Any?, ctx: Any?): String {
            val sb = StringBuilder()
            try {
                val f = common.util.lang.Formatter(pattern, obj, ctx)
                f.root.build(sb)
            } catch (e: Exception) {
                CommonStatic.ctx.noticeErr(e, ErrType.ERROR,
                        "err during formatting " + pattern + " with " + JsonEncoder.Companion.encode(obj))
            }
            return sb.toString()
        }
    }

    init {
        val err = check()
        if (err != null) throw Exception(err)
        root = Formatter.Root(0, str.length)
    }
}