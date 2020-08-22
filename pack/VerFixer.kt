package common.packimport

import common.pack.Context
import common.pack.PackData
import common.pack.Source
import common.pack.UserProfile
import common.util.Data
import common.util.pack.Background
import common.util.unit.Form
import common.util.unit.Unit
import java.io.File
import java.io.InputStream
import java.util.*
import kotlin.collections.HashMap
import kotlin.collections.MutableList
import kotlin.collections.MutableMap
import kotlin.collections.set

com.google.api.client.json.jackson2.JacksonFactoryimport com.google.api.services.drive.DriveScopesimport com.google.api.client.util.store.FileDataStoreFactoryimport com.google.api.client.http.HttpTransportimport com.google.api.services.drive.Driveimport kotlin.Throwsimport java.io.IOExceptionimport io.drive.DriveUtilimport java.io.FileNotFoundExceptionimport java.io.FileInputStreamimport com.google.api.client.googleapis.auth.oauth2.GoogleClientSecretsimport java.io.InputStreamReaderimport com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlowimport com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledAppimport com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiverimport com.google.api.client.googleapis.javanet.GoogleNetHttpTransportimport kotlin.jvm.JvmStaticimport io.drive.DrvieInitimport com.google.api.client.http.javanet.NetHttpTransportimport com.google.api.services.drive.model.FileListimport java.io.BufferedInputStreamimport java.io.FileOutputStreamimport com.google.api.client.googleapis.media.MediaHttpDownloaderimport io.WebFileIOimport io.BCJSONimport page.LoadPageimport org.json.JSONObjectimport org.json.JSONArrayimport main.MainBCUimport main.Optsimport common.CommonStaticimport java.util.TreeMapimport java.util.Arraysimport java.io.BufferedReaderimport io.BCMusicimport common.util.stage.Musicimport io.BCPlayerimport java.util.HashMapimport javax.sound.sampled.Clipimport java.io.ByteArrayInputStreamimport javax.sound.sampled.AudioInputStreamimport javax.sound.sampled.AudioSystemimport javax.sound.sampled.DataLineimport javax.sound.sampled.FloatControlimport javax.sound.sampled.LineEventimport com.google.api.client.googleapis.media.MediaHttpDownloaderProgressListenerimport com.google.api.client.googleapis.media.MediaHttpDownloader.DownloadStateimport common.io.DataIOimport io.BCUReaderimport common.io.InStreamimport com.google.gson.JsonElementimport common.io.json.JsonDecoderimport com.google.gson.JsonObjectimport page.MainFrameimport page.view.ViewBox.Confimport page.MainLocaleimport page.battle.BattleInfoPageimport page.support.Exporterimport page.support.Importerimport common.pack.Context.ErrTypeimport common.util.stage.MapColcimport common.util.stage.MapColc.DefMapColcimport common.util.lang.MultiLangContimport common.util.stage.StageMapimport common.util.unit.Enemyimport io.BCUWriterimport java.text.SimpleDateFormatimport java.io.PrintStreamimport common.io.OutStreamimport common.battle.BasisSetimport res.AnimatedGifEncoderimport java.awt.image.BufferedImageimport javax.imageio.ImageIOimport java.security.MessageDigestimport java.security.NoSuchAlgorithmExceptionimport common.io.json.JsonEncoderimport java.io.FileWriterimport com.google.api.client.http.GenericUrlimport org.apache.http.impl .client.CloseableHttpClientimport org.apache.http.impl .client.HttpClientsimport org.apache.http.client.methods.HttpPostimport org.apache.http.entity.mime.content.FileBodyimport org.apache.http.entity.mime.MultipartEntityBuilderimport org.apache.http.entity.mime.HttpMultipartModeimport org.apache.http.HttpEntityimport org.apache.http.util.EntityUtilsimport com.google.api.client.http.HttpRequestInitializerimport com.google.api.client.http.HttpBackOffUnsuccessfulResponseHandlerimport com.google.api.client.util.ExponentialBackOffimport com.google.api.client.http.HttpBackOffIOExceptionHandlerimport res.NeuQuantimport res.LZWEncoderimport java.io.BufferedOutputStreamimport java.awt.Graphics2Dimport java.awt.image.DataBufferByteimport common.system.fake.FakeImageimport utilpc.awt.FIBIimport jogl.util.AmbImageimport common.system.files.VFileimport jogl.util.GLImageimport com.jogamp.opengl.util.texture.TextureDataimport common.system.Pimport com.jogamp.opengl.util.texture.TextureIOimport jogl.GLStaticimport com.jogamp.opengl.util.texture.awt.AWTTextureIOimport java.awt.AlphaCompositeimport common.system.fake.FakeImage.Markerimport jogl.util.GLGraphicsimport com.jogamp.opengl.GL2import jogl.util.GeoAutoimport com.jogamp.opengl.GL2ES3import com.jogamp.opengl.GLimport common.system.fake.FakeGraphicsimport common.system.fake.FakeTransformimport jogl.util.ResManagerimport jogl.util.GLGraphics.GeomGimport jogl.util.GLGraphics.GLCimport jogl.util.GLGraphics.GLTimport com.jogamp.opengl.GL2ES2import com.jogamp.opengl.util.glsl.ShaderCodeimport com.jogamp.opengl.util.glsl.ShaderProgramimport com.jogamp.opengl.GLExceptionimport jogl.StdGLCimport jogl.Tempimport common.util.anim.AnimUimport common.util.anim.EAnimUimport jogl.util.GLIBimport javax.swing.JFrameimport common.util.anim.AnimCEimport common.util.anim.AnimU.UTypeimport com.jogamp.opengl.util.FPSAnimatorimport com.jogamp.opengl.GLEventListenerimport com.jogamp.opengl.GLAutoDrawableimport page.awt.BBBuilderimport page.battle.BattleBox.OuterBoximport common.battle.SBCtrlimport page.battle.BattleBoximport jogl.GLBattleBoximport common.battle.BattleFieldimport page.anim.IconBoximport jogl.GLIconBoximport jogl.GLBBRecdimport page.awt.RecdThreadimport page.view.ViewBoximport jogl.GLViewBoximport page.view.ViewBox.Controllerimport java.awt.AWTExceptionimport page.battle.BBRecdimport jogl.GLRecorderimport com.jogamp.opengl.GLProfileimport com.jogamp.opengl.GLCapabilitiesimport page.anim.IconBox.IBCtrlimport page.anim.IconBox.IBConfimport page.view.ViewBox.VBExporterimport jogl.GLRecdBImgimport page.JTGimport jogl.GLCstdimport jogl.GLVBExporterimport common.util.anim.EAnimIimport page.RetFuncimport page.battle.BattleBox.BBPainterimport page.battle.BBCtrlimport javax.swing.JOptionPaneimport kotlin.jvm.Strictfpimport main.Invimport javax.swing.SwingUtilitiesimport java.lang.InterruptedExceptionimport utilpc.UtilPC.PCItrimport utilpc.awt.PCIBimport jogl.GLBBBimport page.awt.AWTBBBimport utilpc.Themeimport page.MainPageimport common.io.assets.AssetLoaderimport common.pack.Source.Workspaceimport common.io.PackLoader.ZipDesc.FileDescimport common.io.assets.Adminimport page.awt.BattleBoxDefimport page.awt.IconBoxDefimport page.awt.BBRecdAWTimport page.awt.ViewBoxDefimport org.jcodec.api.awt.AWTSequenceEncoderimport page.awt.RecdThread.PNGThreadimport page.awt.RecdThread.MP4Threadimport page.awt.RecdThread.GIFThreadimport java.awt.GradientPaintimport utilpc.awt.FG2Dimport page.anim.TreeContimport javax.swing.JTreeimport javax.swing.event.TreeExpansionListenerimport common.util.anim.MaModelimport javax.swing.tree.DefaultMutableTreeNodeimport javax.swing.event.TreeExpansionEventimport java.util.function.IntPredicateimport javax.swing.tree.DefaultTreeModelimport common.util.anim.EAnimDimport page.anim.AnimBoximport utilpc.PPimport common.CommonStatic.BCAuxAssetsimport common.CommonStatic.EditLinkimport page.JBTNimport page.anim.DIYViewPageimport page.anim.ImgCutEditPageimport page.anim.MaModelEditPageimport page.anim.MaAnimEditPageimport page.anim.EditHeadimport java.awt.event.ActionListenerimport page.anim.AbEditPageimport common.util.anim.EAnimSimport page.anim.ModelBoximport common.util.anim.ImgCutimport page.view.AbViewPageimport javax.swing.JListimport javax.swing.JScrollPaneimport javax.swing.JComboBoximport utilpc.UtilPCimport javax.swing.event.ListSelectionListenerimport javax.swing.event.ListSelectionEventimport common.system.VImgimport page.support.AnimLCRimport page.support.AnimTableimport common.util.anim.MaAnimimport java.util.EventObjectimport javax.swing.text.JTextComponentimport page.anim.PartEditTableimport javax.swing.ListSelectionModelimport page.support.AnimTableTHimport page.JTFimport utilpc.ReColorimport page.anim.ImgCutEditTableimport page.anim.SpriteBoximport page.anim.SpriteEditPageimport java.awt.event.FocusAdapterimport java.awt.event.FocusEventimport common.pack.PackData.UserPackimport utilpc.Algorithm.SRResultimport page.anim.MaAnimEditTableimport javax.swing.JSliderimport java.awt.event.MouseWheelEventimport common.util.anim.EPartimport javax.swing.event.ChangeEventimport page.anim.AdvAnimEditPageimport javax.swing.BorderFactoryimport page.JLimport javax.swing.ImageIconimport page.anim.MMTreeimport javax.swing.event.TreeSelectionListenerimport javax.swing.event.TreeSelectionEventimport page.support.AbJTableimport page.anim.MaModelEditTableimport page.info.edit.ProcTableimport page.info.edit.ProcTable.AtkProcTableimport page.info.edit.SwingEditorimport page.info.edit.ProcTable.MainProcTableimport page.support.ListJtfPolicyimport page.info.edit.SwingEditor.SwingEGimport common.util.Data.Procimport java.lang.Runnableimport javax.swing.JComponentimport page.info.edit.LimitTableimport page.pack.CharaGroupPageimport page.pack.LvRestrictPageimport javax.swing.SwingConstantsimport common.util.lang.Editors.EditorGroupimport common.util.lang.Editors.EdiFieldimport common.util.lang.Editorsimport common.util.lang.ProcLangimport page.info.edit.EntityEditPageimport common.util.lang.Editors.EditorSupplierimport common.util.lang.Editors.EditControlimport page.info.edit.SwingEditor.IntEditorimport page.info.edit.SwingEditor.BoolEditorimport page.info.edit.SwingEditor.IdEditorimport page.SupPageimport common.util.unit.AbEnemyimport common.pack.IndexContainer.Indexableimport common.pack.Context.SupExcimport common.battle.data .AtkDataModelimport utilpc.Interpretimport common.battle.data .CustomEntityimport page.info.filter.UnitEditBoximport common.battle.data .CustomUnitimport common.util.stage.SCGroupimport page.info.edit.SCGroupEditTableimport common.util.stage.SCDefimport page.info.filter.EnemyEditBoximport common.battle.data .CustomEnemyimport page.info.StageFilterPageimport page.view.BGViewPageimport page.view.CastleViewPageimport page.view.MusicPageimport common.util.stage.CastleImgimport common.util.stage.CastleListimport java.text.DecimalFormatimport common.util.stage.Recdimport common.util.stage.MapColc.PackMapColcimport page.info.edit.StageEditTableimport page.support.ReorderListimport page.info.edit.HeadEditTableimport page.info.filter.EnemyFindPageimport page.battle.BattleSetupPageimport page.info.edit.AdvStEditPageimport page.battle.StRecdPageimport page.info.edit.LimitEditPageimport page.support.ReorderListenerimport common.util.pack.Soulimport page.info.edit.AtkEditTableimport page.info.filter.UnitFindPageimport common.battle.Basisimport common.util.Data.Proc.IMUimport javax.swing.DefaultComboBoxModelimport common.util.Animableimport common.util.pack.Soul.SoulTypeimport page.view.UnitViewPageimport page.view.EnemyViewPageimport page.info.edit.SwingEditor.EditCtrlimport page.support.Reorderableimport page.info.EnemyInfoPageimport common.util.unit.EneRandimport page.pack.EREditPageimport page.support.InTableTHimport page.support.EnemyTCRimport javax.swing.DefaultListCellRendererimport page.info.filter.UnitListTableimport page.info.filter.UnitFilterBoximport page.info.filter.EnemyListTableimport page.info.filter.EnemyFilterBoximport page.info.filter.UFBButtonimport page.info.filter.UFBListimport common.battle.data .MaskUnitimport javax.swing.AbstractButtonimport page.support.SortTableimport page.info.UnitInfoPageimport page.support.UnitTCRimport page.info.filter.EFBButtonimport page.info.filter.EFBListimport common.util.stage.LvRestrictimport common.util.stage.CharaGroupimport page.info.StageTableimport page.info.TreaTableimport javax.swing.JPanelimport page.info.UnitInfoTableimport page.basis.BasisPageimport kotlin.jvm.JvmOverloadsimport page.info.EnemyInfoTableimport common.util.stage.RandStageimport page.info.StagePageimport page.info.StageRandPageimport common.util.unit.EFormimport page.pack.EREditTableimport common.util.EREntimport common.pack.FixIndexListimport page.support.UnitLCRimport page.pack.RecdPackPageimport page.pack.CastleEditPageimport page.pack.BGEditPageimport page.pack.CGLREditPageimport common.pack.Source.ZipSourceimport page.info.edit.EnemyEditPageimport page.info.edit.StageEditPageimport page.info.StageViewPageimport page.pack.UnitManagePageimport page.pack.MusicEditPageimport page.battle.AbRecdPageimport common.system.files.VFileRootimport java.awt.Desktopimport common.pack.PackDataimport common.util.unit.UnitLevelimport page.info.edit.FormEditPageimport common.util.anim.AnimIimport common.util.anim.AnimI.AnimTypeimport common.util.anim.AnimDimport common.battle.data .Orbimport page.basis.LineUpBoximport page.basis.LubContimport common.battle.BasisLUimport page.basis.ComboListTableimport page.basis.ComboListimport page.basis.NyCasBoximport page.basis.UnitFLUPageimport common.util.unit.Comboimport page.basis.LevelEditPageimport common.util.pack.NyCastleimport common.battle.LineUpimport common.system.SymCoordimport java.util.TreeSetimport page.basis.OrbBoximport javax.swing.table.DefaultTableCellRendererimport javax.swing.JTableimport common.CommonStatic.BattleConstimport common.battle.StageBasisimport common.util.ImgCoreimport common.battle.attack.ContAbimport common.battle.entity.EAnimContimport common.battle.entity.WaprContimport page.battle.RecdManagePageimport page.battle.ComingTableimport common.util.stage.EStageimport page.battle.EntityTableimport common.battle.data .MaskEnemyimport common.battle.SBRplyimport common.battle.entity.AbEntityimport page.battle.RecdSavePageimport page.LocCompimport page.LocSubCompimport javax.swing.table.TableModelimport page.support.TModelimport javax.swing.event.TableModelListenerimport javax.swing.table.DefaultTableColumnModelimport javax.swing.JFileChooserimport javax.swing.filechooser.FileNameExtensionFilterimport javax.swing.TransferHandlerimport java.awt.datatransfer.Transferableimport java.awt.datatransfer.DataFlavorimport javax.swing.DropModeimport javax.swing.TransferHandler.TransferSupportimport java.awt.dnd.DragSourceimport java.awt.datatransfer.UnsupportedFlavorExceptionimport common.system.Copableimport page.support.AnimTransferimport javax.swing.DefaultListModelimport page.support.InListTHimport java.awt.FocusTraversalPolicyimport javax.swing.JTextFieldimport page.CustomCompimport javax.swing.JToggleButtonimport javax.swing.JButtonimport javax.swing.ToolTipManagerimport javax.swing.JRootPaneimport javax.swing.JProgressBarimport page.ConfigPageimport page.view.EffectViewPageimport page.pack.PackEditPageimport page.pack.ResourcePageimport javax.swing.WindowConstantsimport java.awt.event.AWTEventListenerimport java.awt.AWTEventimport java.awt.event.ComponentAdapterimport java.awt.event.ComponentEventimport java.util.ConcurrentModificationExceptionimport javax.swing.plaf.FontUIResourceimport java.util.Enumerationimport javax.swing.UIManagerimport common.CommonStatic.FakeKeyimport page.LocSubComp.LocBinderimport page.LSCPopimport java.awt.BorderLayoutimport java.awt.GridLayoutimport javax.swing.JTextPaneimport page.TTTimport java.util.ResourceBundleimport java.util.MissingResourceExceptionimport java.util.Localeimport common.io.json.Test.JsonTest_2import common.pack.PackData.PackDescimport common.io.PackLoaderimport common.io.PackLoader.Preloadimport common.io.PackLoader.ZipDescimport common.io.json.Testimport common.io.json.JsonClassimport common.io.json.JsonFieldimport common.io.json.JsonField.GenTypeimport common.io.json.Test.JsonTest_0.JsonDimport common.io.json.JsonClass.RTypeimport java.util.HashSetimport common.io.json.JsonDecoder.OnInjectedimport common.io.json.JsonField.IOTypeimport common.io.json.JsonExceptionimport common.io.json.JsonClass.NoTagimport common.io.json.JsonField.SerTypeimport common.io.json.JsonClass.WTypeimport kotlin.reflect.KClassimport com.google.gson.JsonArrayimport common.io.assets.Admin.StaticPermittedimport common.io.json.JsonClass.JCGenericimport common.io.json.JsonClass.JCGetterimport com.google.gson.JsonPrimitiveimport com.google.gson.JsonNullimport common.io.json.JsonClass.JCIdentifierimport java.lang.ClassNotFoundExceptionimport common.io.assets.AssetLoader.AssetHeaderimport common.io.assets.AssetLoader.AssetHeader.AssetEntryimport common.io.InStreamDefimport common.io.BCUExceptionimport java.io.UnsupportedEncodingExceptionimport common.io.OutStreamDefimport javax.crypto.Cipherimport javax.crypto.spec.IvParameterSpecimport javax.crypto.spec.SecretKeySpecimport common.io.PackLoader.FileSaverimport common.system.files.FDByteimport common.io.json.JsonClass.JCConstructorimport common.io.PackLoader.FileLoader.FLStreamimport common.io.PackLoader.PatchFileimport java.lang.NullPointerExceptionimport java.lang.IndexOutOfBoundsExceptionimport common.io.MultiStreamimport java.io.RandomAccessFileimport common.io.MultiStream.TrueStreamimport java.lang.RuntimeExceptionimport common.pack.Source.ResourceLocationimport common.pack.Source.AnimLoaderimport common.pack.Source.SourceAnimLoaderimport common.util.anim.AnimCIimport common.system.files.FDFileimport common.pack.IndexContainerimport common.battle.data .PCoinimport common.util.pack.EffAnimimport common.battle.data .DataEnemyimport common.util.stage.Limit.DefLimitimport common.pack.IndexContainer.Reductorimport common.pack.FixIndexList.FixIndexMapimport common.pack.VerFixer.IdFixerimport common.pack.IndexContainer.IndexContimport common.pack.IndexContainer.ContGetterimport common.util.stage.CastleList.PackCasListimport common.util.Data.Proc.THEMEimport common.CommonStatic.ImgReaderimport common.pack.VerFixerimport common.pack.VerFixer.VerFixerExceptionimport java.lang.NumberFormatExceptionimport common.pack.Source.SourceAnimSaverimport common.pack.VerFixer.EnemyFixerimport common.pack.VerFixer.PackFixerimport common.pack.PackData.DefPackimport java.util.function.BiConsumerimport common.util.BattleStaticimport common.util.anim.AnimU.ImageKeeperimport common.util.anim.AnimCE.AnimCELoaderimport common.util.anim.AnimCI.AnimCIKeeperimport common.util.anim.AnimUD.DefImgLoaderimport common.util.BattleObjimport common.util.Data.Proc.ProcItemimport common.util.lang.ProcLang.ItemLangimport common.util.lang.LocaleCenter.Displayableimport common.util.lang.Editors.DispItemimport common.util.lang.LocaleCenter.ObjBinderimport common.util.lang.LocaleCenter.ObjBinder.BinderFuncimport common.util.Data.Proc.PROBimport org.jcodec.common.tools.MathUtilimport common.util.Data.Proc.PTimport common.util.Data.Proc.PTDimport common.util.Data.Proc.PMimport common.util.Data.Proc.WAVEimport common.util.Data.Proc.WEAKimport common.util.Data.Proc.STRONGimport common.util.Data.Proc.BURROWimport common.util.Data.Proc.REVIVEimport common.util.Data.Proc.SUMMONimport common.util.Data.Proc.MOVEWAVEimport common.util.Data.Proc.POISONimport common.util.Data.Proc.CRITIimport common.util.Data.Proc.VOLCimport common.util.Data.Proc.ARMORimport common.util.Data.Proc.SPEEDimport java.util.LinkedHashMapimport common.util.lang.LocaleCenter.DisplayItemimport common.util.lang.ProcLang.ProcLangStoreimport common.util.lang.Formatter.IntExpimport common.util.lang.Formatter.RefObjimport common.util.lang.Formatter.BoolExpimport common.util.lang.Formatter.BoolElemimport common.util.lang.Formatter.IElemimport common.util.lang.Formatter.Contimport common.util.lang.Formatter.Elemimport common.util.lang.Formatter.RefElemimport common.util.lang.Formatter.RefFieldimport common.util.lang.Formatter.RefFuncimport common.util.lang.Formatter.TextRefimport common.util.lang.Formatter.CodeBlockimport common.util.lang.Formatter.TextPlainimport common.util.unit.Unit.UnitInfoimport common.util.lang.MultiLangCont.MultiLangStaticsimport common.util.pack.EffAnim.EffTypeimport common.util.pack.EffAnim.ArmorEffimport common.util.pack.EffAnim.BarEneEffimport common.util.pack.EffAnim.BarrierEffimport common.util.pack.EffAnim.DefEffimport common.util.pack.EffAnim.WarpEffimport common.util.pack.EffAnim.ZombieEffimport common.util.pack.EffAnim.KBEffimport common.util.pack.EffAnim.SniperEffimport common.util.pack.EffAnim.VolcEffimport common.util.pack.EffAnim.SpeedEffimport common.util.pack.EffAnim.WeakUpEffimport common.util.pack.EffAnim.EffAnimStoreimport common.util.pack.NyCastle.NyTypeimport common.util.pack.WaveAnimimport common.util.pack.WaveAnim.WaveTypeimport common.util.pack.Background.BGWvTypeimport common.util.unit.Form.FormJsonimport common.system.BasedCopableimport common.util.anim.AnimUDimport common.battle.data .DataUnitimport common.battle.entity.EUnitimport common.battle.entity.EEnemyimport common.util.EntRandimport common.util.stage.Recd.Waitimport java.lang.CloneNotSupportedExceptionimport common.util.stage.StageMap.StageMapInfoimport common.util.stage.Stage.StageInfoimport common.util.stage.Limit.PackLimitimport common.util.stage.MapColc.ClipMapColcimport common.util.stage.CastleList.DefCasListimport common.util.stage.MapColc.StItrimport common.util.Data.Proc.IntType.BitCountimport common.util.CopRandimport common.util.LockGLimport java.lang.IllegalAccessExceptionimport common.battle.data .MaskAtkimport common.battle.data .DefaultDataimport common.battle.data .DataAtkimport common.battle.data .MaskEntityimport common.battle.data .DataEntityimport common.battle.attack.AtkModelAbimport common.battle.attack.AttackAbimport common.battle.attack.AttackSimpleimport common.battle.attack.AttackWaveimport common.battle.entity.Cannonimport common.battle.attack.AttackVolcanoimport common.battle.attack.ContWaveAbimport common.battle.attack.ContWaveDefimport common.battle.attack.AtkModelEntityimport common.battle.entity.EntContimport common.battle.attack.ContMoveimport common.battle.attack.ContVolcanoimport common.battle.attack.ContWaveCanonimport common.battle.attack.AtkModelEnemyimport common.battle.attack.AtkModelUnitimport common.battle.attack.AttackCanonimport common.battle.entity.EUnit.OrbHandlerimport common.battle.entity.Entity.AnimManagerimport common.battle.entity.Entity.AtkManagerimport common.battle.entity.Entity.ZombXimport common.battle.entity.Entity.KBManagerimport common.battle.entity.Entity.PoisonTokenimport common.battle.entity.Entity.WeakTokenimport common.battle.Treasureimport common.battle.MirrorSetimport common.battle.Releaseimport common.battle.ELineUpimport common.battle.entity.Sniperimport common.battle.entity.ECastleimport java.util.Dequeimport common.CommonStatic.Itfimport java.lang.Characterimport common.CommonStatic.ImgWriterimport utilpc.awt.FTATimport utilpc.awt.Blenderimport java.awt.RenderingHintsimport utilpc.awt.BIBuilderimport java.awt.CompositeContextimport java.awt.image.Rasterimport java.awt.image.WritableRasterimport utilpc.ColorSetimport utilpc.OggTimeReaderimport utilpc.UtilPC.PCItr.MusicReaderimport utilpc.UtilPC.PCItr.PCALimport javax.swing.UIManager.LookAndFeelInfoimport java.lang.InstantiationExceptionimport javax.swing.UnsupportedLookAndFeelExceptionimport utilpc.Algorithm.ColorShiftimport utilpc.Algorithm.StackRect
abstract class VerFixer private constructor(id: String) : Source(id) {
    class IdFixer(cls: Class<*>?) {
        private val ent: Class<*>
        fun parse(`val`: Int, cls: Class<*>): Class<*> {
            return if (cls == THEME::class.java) Background::class.java else if (ent == Unit::class.java) ent else if (`val` % 1000 < 500) Enemy::class.java else EneRand::class.java
        }

        init {
            ent = cls ?: AbEnemy::class.java
        }
    }

    class VerFixerException(str: String?) : Exception(str) {
        companion object {
            private const val serialVersionUID = 1L
        }
    }

    private class EnemyFixer(id: String, r: ImgReader?) : VerFixer(id) {
        @Throws(VerFixerException::class)
        override fun load() {
            loadEnemies(`is`.subStream())
            loadUnits(`is`.subStream())
            loadBackgrounds(`is`.subStream())
            loadCastles(`is`.subStream())
            loadMusics(`is`.subStream())
            data.mc = PackMapColc(data, `is`)
            `is` = null
        }

        @Throws(VerFixerException::class)
        private fun loadAnim(`is`: InStream): AnimCE {
            val ver: Int = Data.Companion.getVer(`is`.nextString())
            if (ver != 401) throw VerFixerException("DIYAnim expects version 401, got $ver")
            val type: Int = `is`.nextInt()
            if (type == 0) return AnimCE(`is`.nextString()) else if (type == 1) return decodeAnim(id, `is`.subStream(), r)
            throw VerFixerException("DIYAnim expects type 0 or 1, got " + 2)
        }

        @Throws(VerFixerException::class)
        private fun loadBackgrounds(`is`: InStream) {
            val ver: Int = Data.Companion.getVer(`is`.nextString())
            if (ver != 400) throw VerFixerException("expect bg store version to be 400, got $ver")
            val f: File = CommonStatic.def.route("./res/img/$id/bg/")
            if (f.exists()) {
                val fs = f.listFiles()
                for (fi in fs) {
                    val str = fi.name
                    if (str.length != 7) continue
                    if (!str.endsWith(".png")) continue
                    var `val` = -1
                    `val` = try {
                        str.substring(0, 3).toInt()
                    } catch (e: NumberFormatException) {
                        continue
                    }
                    val fx: File = CommonStatic.ctx.getWorkspaceFile("./$id/backgrounds/$str")
                    fi.renameTo(fx)
                    val bimg: VImg = CommonStatic.def.readReal(fx)
                    if (`val` >= 0 && bimg != null) data.bgs.set(`val`, Background(PackData.Identifier(id, Background::class.java, `val`), bimg))
                }
            }
            val n: Int = `is`.nextInt()
            for (i in 0 until n) {
                val ind: Int = `is`.nextInt()
                val bg: Background = data.bgs.get(ind) ?: continue
                bg.top = `is`.nextInt() > 0
                bg.ic = `is`.nextInt()
                for (j in 0..3) {
                    val p: Int = `is`.nextInt()
                    bg.cs[j] = intArrayOf(p shr 16 and 255, p shr 8 and 255, p and 255)
                }
            }
        }

        @Throws(VerFixerException::class)
        private fun loadCastles(`is`: InStream) {
            val ver: Int = Data.Companion.getVer(`is`.nextString())
            if (ver != 307) throw VerFixerException("expect castle store version to be 307, got $ver")
            `is`.nextInt()
            val f: File = CommonStatic.def.route("./res/img/$id/cas/")
            if (f.exists()) {
                val fs = f.listFiles()
                for (fi in fs) {
                    val str = fi.name
                    if (str.length != 7) continue
                    if (!str.endsWith(".png")) continue
                    var `val` = -1
                    `val` = try {
                        str.substring(0, 3).toInt()
                    } catch (e: NumberFormatException) {
                        e.printStackTrace()
                        continue
                    }
                    val fx: File = CommonStatic.ctx.getWorkspaceFile("./$id/castles/$str")
                    fi.renameTo(fx)
                    val bimg: VImg = CommonStatic.def.readReal(fx)
                    if (`val` >= 0 && bimg != null) data.castles.set(`val`, CastleImg(PackData.Identifier<CastleImg>(id, CastleImg::class.java, `val`), bimg))
                }
            }
        }

        @Throws(VerFixerException::class)
        private fun loadEnemies(`is`: InStream) {
            val ver: Int = Data.Companion.getVer(`is`.nextString())
            if (ver != 402) throw VerFixerException("expect enemy store version to be 402, got $ver")
            UserProfile.Companion.setStatic(PackData.Identifier.Companion.STATIC_FIXER, IdFixer(AbEnemy::class.java))
            val len: Int = `is`.nextInt()
            for (i in 0 until len) {
                val ce = CustomEnemy()
                ce.fillData(402, `is`)
                val hash: Int = `is`.nextInt()
                val anim: InStream = `is`.subStream()
                val na: String = `is`.nextString()
                val ac: AnimCE = loadAnim(anim)
                val e = Enemy(PackData.Identifier<AbEnemy>(id, Enemy::class.java, hash), ac, ce)
                e.name = na
                data.enemies.set(hash % 1000, e)
            }
            val n: Int = `is`.nextInt()
            for (i in 0 until n) {
                val hash: Int = `is`.nextInt()
                val e = EneRand(PackData.Identifier<AbEnemy>(id, EneRand::class.java, hash + 500)) // TODO rand enemies
                e.zread(`is`.subStream())
                data.randEnemies.set(hash, e)
            }
        }

        private fun loadMusics(`is`: InStream) {
            val f: File = CommonStatic.def.route("./res/img/$id/music/")
            if (f.exists() && f.isDirectory) {
                val fs = f.listFiles()
                for (fi in fs) {
                    val str = fi.name
                    if (str.length != 7) continue
                    if (!str.endsWith(".ogg")) continue
                    var `val` = -1
                    `val` = try {
                        str.substring(0, 3).toInt()
                    } catch (e: NumberFormatException) {
                        e.printStackTrace()
                        continue
                    }
                    val fx: File = CommonStatic.ctx.getWorkspaceFile("./$id/musics/$str")
                    fi.renameTo(fx)
                    if (`val` >= 0) data.musics.set(`val`, Music(PackData.Identifier<Music>(id, Music::class.java, `val`), FDFile(fx)))
                }
            }
        }

        @Throws(VerFixerException::class)
        private fun loadUnits(`is`: InStream) {
            val ver: Int = Data.Companion.getVer(`is`.nextString())
            if (ver != 401) throw VerFixerException("expect unit store version to be 401, got $ver")
            UserProfile.Companion.setStatic(PackData.Identifier.Companion.STATIC_FIXER, IdFixer(Unit::class.java))
            var n: Int = `is`.nextInt()
            for (i in 0 until n) {
                val ind: Int = `is`.nextInt()
                val ul = UnitLevel(PackData.Identifier<UnitLevel>(id, UnitLevel::class.java, ind), `is`)
                data.unitLevels.set(ind, ul)
            }
            n = `is`.nextInt()
            for (i in 0 until n) {
                val ind: Int = `is`.nextInt()
                val u = Unit(PackData.Identifier(id, Unit::class.java, ind))
                u.lv = PackData.Identifier.Companion.parseInt<UnitLevel>(`is`.nextInt(), UnitLevel::class.java).get()
                u.lv.units.add(u)
                u.max = `is`.nextInt()
                u.maxp = `is`.nextInt()
                u.rarity = `is`.nextInt()
                val m: Int = `is`.nextInt()
                u.forms = arrayOfNulls(m)
                for (j in 0 until m) {
                    val name: String = `is`.nextString()
                    val ac: AnimCE = loadAnim(`is`.subStream())
                    val cu = CustomUnit()
                    cu.fillData(401, `is`)
                    u.forms[j] = Form(u, j, name, ac, cu)
                }
                data.units.set(ind, u)
            }
            UserProfile.Companion.setStatic(PackData.Identifier.Companion.STATIC_FIXER, null)
        }

        init {
            this.r = r
        }
    }

    private class PackFixer(id: String, r: ImgReader?) : VerFixer(id) {
        @Throws(Exception::class)
        override fun load() {
            loadEnemies(`is`.subStream())
            loadUnits(`is`.subStream())
            loadBackgrounds(`is`.subStream())
            loadCastles(`is`.subStream())
            loadMusics(`is`.subStream())
            data.mc = PackMapColc(data, `is`)
            `is` = null
        }

        @Throws(Exception::class)
        private fun loadBackgrounds(`is`: InStream) {
            val ver: Int = Data.Companion.getVer(`is`.nextString())
            if (ver != 400) throw VerFixerException("expect bg store version to be 400, got $ver")
            val n: Int = `is`.nextInt()
            for (i in 0 until n) {
                val ind: Int = `is`.nextInt()
                val vimg: VImg = ImgReader.Companion.readImg(`is`, r)
                vimg.name = Data.Companion.trio(ind)
                writeImgs(vimg, "backgrounds", vimg.name + ".png")
                val bg = Background(PackData.Identifier(id, Background::class.java, ind), vimg)
                data.bgs.set(ind, bg)
                bg.top = `is`.nextInt() > 0
                bg.ic = `is`.nextInt()
                for (j in 0..3) {
                    val p: Int = `is`.nextInt()
                    bg.cs[j] = intArrayOf(p shr 8 and 255, p shr 8 and 255, p and 255)
                }
            }
        }

        @Throws(Exception::class)
        private fun loadCastles(`is`: InStream) {
            val ver: Int = Data.Companion.getVer(`is`.nextString())
            if (ver != 307) throw VerFixerException("expect castle store version to be 307, got $ver")
            val n: Int = `is`.nextInt()
            for (i in 0 until n) {
                val `val`: Int = `is`.nextInt()
                val vimg: VImg = ImgReader.Companion.readImg(`is`, r)
                vimg.name = Data.Companion.trio(`val`)
                writeImgs(vimg, "castles", vimg.name + ".png")
                data.castles.set(`val`, CastleImg(PackData.Identifier<CastleImg>(id, CastleImg::class.java, `val`), vimg))
            }
        }

        @Throws(VerFixerException::class)
        private fun loadEnemies(`is`: InStream) {
            val ver: Int = Data.Companion.getVer(`is`.nextString())
            if (ver != 402) throw VerFixerException("expect enemy store version to be 402, got $ver")
            UserProfile.Companion.setStatic(PackData.Identifier.Companion.STATIC_FIXER, IdFixer(AbEnemy::class.java))
            var n: Int = `is`.nextInt()
            for (i in 0 until n) {
                val hash: Int = `is`.nextInt()
                val str: String = `is`.nextString()
                val ce = CustomEnemy()
                ce.fillData(ver, `is`)
                val ac: AnimCE = decodeAnim(".temp_$id", `is`.subStream(), r)
                val e = Enemy(PackData.Identifier<AbEnemy>(id, Enemy::class.java, hash), ac, ce)
                e.name = str
                data.enemies.set(hash % 1000, e)
            }
            n = `is`.nextInt()
            for (i in 0 until n) {
                val hash: Int = `is`.nextInt()
                val e = EneRand(PackData.Identifier<AbEnemy>(id, EneRand::class.java, hash + 500)) // TODO randEnemy
                e.zread(`is`.subStream())
                data.randEnemies.set(hash, e)
            }
        }

        @Throws(VerFixerException::class)
        private fun loadMusics(`is`: InStream) {
            val ver: Int = Data.Companion.getVer(`is`.nextString())
            if (ver != 307) throw VerFixerException("expect music store version to be 307, got $ver")
            val n: Int = `is`.nextInt()
            for (i in 0 until n) {
                val `val`: Int = `is`.nextInt()
                val f: File = ImgReader.Companion.loadMusicFile(`is`, r, id.toInt(), `val`)
                f.renameTo(CommonStatic.ctx.getWorkspaceFile("./.temp_" + `val` + "/musics/" + Data.Companion.trio(`val`) + ".ogg"))
                data.musics.set(`val`, Music(PackData.Identifier<Music>(id, Music::class.java, `val`), FDFile(f)))
            }
        }

        @Throws(VerFixerException::class)
        private fun loadUnits(`is`: InStream) {
            val ver: Int = Data.Companion.getVer(`is`.nextString())
            if (ver != 401) throw VerFixerException("expect unit store version to be 401, got $ver")
            UserProfile.Companion.setStatic(PackData.Identifier.Companion.STATIC_FIXER, IdFixer(Unit::class.java))
            var n: Int = `is`.nextInt()
            for (i in 0 until n) {
                val ind: Int = `is`.nextInt()
                val ul = UnitLevel(PackData.Identifier<UnitLevel>(id, UnitLevel::class.java, ind), `is`)
                data.unitLevels.set(ind, ul)
            }
            n = `is`.nextInt()
            for (i in 0 until n) {
                val ind: Int = `is`.nextInt()
                val u = Unit(PackData.Identifier(id, Unit::class.java, ind))
                u.lv = PackData.Identifier.Companion.parseInt<UnitLevel>(`is`.nextInt(), UnitLevel::class.java).get()
                u.lv.units.add(u)
                u.max = `is`.nextInt()
                u.maxp = `is`.nextInt()
                u.rarity = `is`.nextInt()
                val m: Int = `is`.nextInt()
                u.forms = arrayOfNulls(m)
                for (j in 0 until m) {
                    val name: String = `is`.nextString()
                    val ac: AnimCE = decodeAnim(".temp_$id", `is`.subStream(), r)
                    val cu = CustomUnit()
                    cu.fillData(401, `is`)
                    u.forms[j] = Form(u, j, name, ac, cu)
                }
                data.units.set(ind, u)
            }
            UserProfile.Companion.setStatic(PackData.Identifier.Companion.STATIC_FIXER, null)
        }

        @Throws(IOException::class)
        private fun writeImgs(img: VImg, type: String, name: String) {
            val path = "./.temp_$id/$type/$name"
            val f: File = CommonStatic.ctx.getWorkspaceFile(path)
            FakeImage.Companion.write(img.getImg(), "", f)
            img.unload()
        }

        init {
            this.r = r
        }
    }

    var r: ImgReader? = null
    var data: UserPack? = null
    var `is`: InStream? = null
    override fun loadAnimation(name: String?): AnimCI? {
        return null
    }

    @Throws(Exception::class)
    override fun readImage(path: String): FakeImage? {
        return null
    }

    @Throws(Exception::class)
    override fun streamFile(path: String): InputStream? {
        return null
    }

    protected fun decodeAnim(target: String?, `is`: InStream?, r: ImgReader?): AnimCE {
        val al: AnimLoader = CommonStatic.def.loadAnim(`is`, r)
        val id = al.name
        id.pack = target
        val ce = AnimCE(al)
        SourceAnimSaver(id, ce).saveAll()
        return AnimCE(id)
    }

    @Throws(Exception::class)
    protected abstract fun load()

    companion object {
        @Throws(Exception::class)
        fun fix() {
            transform()
            readPacks()
            Context.Companion.delete(File("./res"))
            Context.Companion.delete(File("./pack"))
        }

        @Throws(VerFixerException::class)
        private fun fix_bcuenemy(`is`: InStream, r: ImgReader): VerFixer {
            val ver: Int = Data.Companion.getVer(`is`.nextString())
            if (ver != 400) throw VerFixerException("unexpected bcuenemy data version: $ver, requires 400")
            val desc = PackDesc(Data.Companion.hex(`is`.nextInt()))
            val n: Int = `is`.nextByte()
            for (i in 0 until n) desc.dependency.add(Data.Companion.hex(`is`.nextInt()))
            val fix = EnemyFixer(desc.id, r)
            val ans = UserPack(desc, fix)
            fix.data = ans
            fix.`is` = `is`
            return fix
        }

        @Throws(Exception::class)
        private fun fix_bcupack(`is`: InStream, r: ImgReader): VerFixer {
            val ver: Int = Data.Companion.getVer(`is`.nextString())
            if (ver != 402) throw VerFixerException("unexpected bcupack data version: $ver, requires 402")
            val head: InStream = `is`.subStream()
            val desc = PackDesc(Data.Companion.hex(head.nextInt()))
            val n: Int = head.nextByte()
            for (i in 0 until n) desc.dependency.add(Data.Companion.hex(head.nextInt()))
            desc.BCU_VERSION = Data.Companion.revVer(head.nextInt())
            if (!desc.BCU_VERSION.startsWith("4.11")) VerFixerException("unexpected pack BCU version: " + desc.BCU_VERSION + ", requires 4.11.x")
            desc.time = head.nextString()
            desc.version = head.nextInt()
            desc.author = head.nextString()
            val fix = PackFixer(desc.id, r)
            val ans = UserPack(desc, fix)
            fix.data = ans
            fix.`is` = `is`
            return fix
        }

        private fun move(a: String, b: String) {
            val f = File(a)
            if (!f.exists()) return
            f.renameTo(File(b))
        }

        @Throws(Exception::class)
        private fun readPacks() {
            var f: File = CommonStatic.def.route("./pack/")
            val fmap: MutableMap<String, File> = HashMap()
            val map: MutableMap<String, VerFixer> = HashMap()
            if (f.exists()) {
                for (file in f.listFiles()) {
                    val str = file.name
                    if (!str.endsWith(".bcupack")) continue
                    var g: File = CommonStatic.def.route("./res/data/" + str.replace(".bcupack", ".bcudata"))
                    if (!g.exists()) g = file
                    val pack = fix_bcupack(CommonStatic.def.readBytes(g), CommonStatic.def.getReader(g))
                    fmap[pack.id] = file
                    map[pack.id] = pack
                }
            }
            val list: MutableList<VerFixer> = ArrayList()
            list.addAll(map.values)
            f = CommonStatic.def.route("./res/enemy/")
            if (f.exists()) for (file in f.listFiles()) {
                val str = file.name
                if (!str.endsWith(".bcuenemy")) continue
                val fix = fix_bcuenemy(CommonStatic.def.readBytes(file), CommonStatic.def.getReader(file))
                list.removeIf { p: VerFixer -> p.id === fix.id }
                list.add(fix)
            }
            while (list.size > 0) {
                var rem = 0
                for (p in list) {
                    var all = true
                    for (pre in p.data.desc.dependency) if (!map.containsKey(pre) || map[pre]!!.`is` != null) all = false
                    if (all) {
                        p.load()
                        rem++
                    }
                }
                list.removeIf { p: VerFixer -> p.`is` == null }
                if (rem == 0) {
                    for (p in list) {
                        map.remove(p.id)
                        CommonStatic.ctx.printErr(ErrType.WARN, "Failed to load " + p.data.desc)
                    }
                    break
                }
            }
        }

        @Throws(IOException::class)
        private fun transform() {
            val f = File("./res/anim/")
            for (fi in f.list()) {
                val pa = "./res/anim/$fi/"
                val pb = "./workspace/_local/$fi/"
                move("$pa$fi.png", pb + SourceAnimLoader.Companion.SP)
                move(pa + "edi.png", pb + SourceAnimLoader.Companion.EDI)
                move(pa + "uni.png", pb + SourceAnimLoader.Companion.UNI)
                move("$pa$fi.imgcut", pb + SourceAnimLoader.Companion.IC)
                move("$pa$fi.mamodel", pb + SourceAnimLoader.Companion.MM)
                move(pa + fi + "00.maanim", pb + SourceAnimLoader.Companion.MA.get(0))
                move(pa + fi + "01.maanim", pb + SourceAnimLoader.Companion.MA.get(1))
                move(pa + fi + "02.maanim", pb + SourceAnimLoader.Companion.MA.get(2))
                move(pa + fi + "03.maanim", pb + SourceAnimLoader.Companion.MA.get(3))
                move(pa + fi + "_zombie00.maanim", pb + SourceAnimLoader.Companion.MA.get(4))
                move(pa + fi + "_zombie01.maanim", pb + SourceAnimLoader.Companion.MA.get(5))
                move(pa + fi + "_zombie02.maanim", pb + SourceAnimLoader.Companion.MA.get(6))
            }
        }
    }
}