package common.io.jsonimport

import common.io.assets.Admin
import common.io.json.JsonException
import common.io.json.JsonField
import common.util.Data
import java.lang.annotation.Documented
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy
import java.lang.reflect.*
import java.lang.reflect.Array

com.google.api.client.json.jackson2.JacksonFactoryimport com.google.api.services.drive.DriveScopesimport com.google.api.client.util.store.FileDataStoreFactoryimport com.google.api.client.http.HttpTransportimport com.google.api.services.drive.Driveimport kotlin.Throwsimport java.io.IOExceptionimport io.drive.DriveUtilimport java.io.FileNotFoundExceptionimport java.io.FileInputStreamimport com.google.api.client.googleapis.auth.oauth2.GoogleClientSecretsimport java.io.InputStreamReaderimport com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlowimport com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledAppimport com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiverimport com.google.api.client.googleapis.javanet.GoogleNetHttpTransportimport kotlin.jvm.JvmStaticimport io.drive.DrvieInitimport com.google.api.client.http.javanet.NetHttpTransportimport com.google.api.services.drive.model.FileListimport java.io.BufferedInputStreamimport java.io.FileOutputStreamimport com.google.api.client.googleapis.media.MediaHttpDownloaderimport io.WebFileIOimport io.BCJSONimport page.LoadPageimport org.json.JSONObjectimport org.json.JSONArrayimport main.MainBCUimport main.Optsimport common.CommonStaticimport java.util.TreeMapimport java.util.Arraysimport java.io.BufferedReaderimport io.BCMusicimport common.util.stage.Musicimport io.BCPlayerimport java.util.HashMapimport javax.sound.sampled.Clipimport java.io.ByteArrayInputStreamimport javax.sound.sampled.AudioInputStreamimport javax.sound.sampled.AudioSystemimport javax.sound.sampled.DataLineimport javax.sound.sampled.FloatControlimport javax.sound.sampled.LineEventimport com.google.api.client.googleapis.media.MediaHttpDownloaderProgressListenerimport com.google.api.client.googleapis.media.MediaHttpDownloader.DownloadStateimport common.io.DataIOimport io.BCUReaderimport common.io.InStreamimport com.google.gson.JsonElementimport common.io.json.JsonDecoderimport com.google.gson.JsonObjectimport page.MainFrameimport page.view.ViewBox.Confimport page.MainLocaleimport page.battle.BattleInfoPageimport page.support.Exporterimport page.support.Importerimport common.pack.Context.ErrTypeimport common.util.stage.MapColcimport common.util.stage.MapColc.DefMapColcimport common.util.lang.MultiLangContimport common.util.stage.StageMapimport common.util.unit.Enemyimport io.BCUWriterimport java.text.SimpleDateFormatimport java.io.PrintStreamimport common.io.OutStreamimport common.battle.BasisSetimport res.AnimatedGifEncoderimport java.awt.image.BufferedImageimport javax.imageio.ImageIOimport java.security.MessageDigestimport java.security.NoSuchAlgorithmExceptionimport common.io.json.JsonEncoderimport java.io.FileWriterimport com.google.api.client.http.GenericUrlimport org.apache.http.impl .client.CloseableHttpClientimport org.apache.http.impl .client.HttpClientsimport org.apache.http.client.methods.HttpPostimport org.apache.http.entity.mime.content.FileBodyimport org.apache.http.entity.mime.MultipartEntityBuilderimport org.apache.http.entity.mime.HttpMultipartModeimport org.apache.http.HttpEntityimport org.apache.http.util.EntityUtilsimport com.google.api.client.http.HttpRequestInitializerimport com.google.api.client.http.HttpBackOffUnsuccessfulResponseHandlerimport com.google.api.client.util.ExponentialBackOffimport com.google.api.client.http.HttpBackOffIOExceptionHandlerimport res.NeuQuantimport res.LZWEncoderimport java.io.BufferedOutputStreamimport java.awt.Graphics2Dimport java.awt.image.DataBufferByteimport common.system.fake.FakeImageimport utilpc.awt.FIBIimport jogl.util.AmbImageimport common.system.files.VFileimport jogl.util.GLImageimport com.jogamp.opengl.util.texture.TextureDataimport common.system.Pimport com.jogamp.opengl.util.texture.TextureIOimport jogl.GLStaticimport com.jogamp.opengl.util.texture.awt.AWTTextureIOimport java.awt.AlphaCompositeimport common.system.fake.FakeImage.Markerimport jogl.util.GLGraphicsimport com.jogamp.opengl.GL2import jogl.util.GeoAutoimport com.jogamp.opengl.GL2ES3import com.jogamp.opengl.GLimport common.system.fake.FakeGraphicsimport common.system.fake.FakeTransformimport jogl.util.ResManagerimport jogl.util.GLGraphics.GeomGimport jogl.util.GLGraphics.GLCimport jogl.util.GLGraphics.GLTimport com.jogamp.opengl.GL2ES2import com.jogamp.opengl.util.glsl.ShaderCodeimport com.jogamp.opengl.util.glsl.ShaderProgramimport com.jogamp.opengl.GLExceptionimport jogl.StdGLCimport jogl.Tempimport common.util.anim.AnimUimport common.util.anim.EAnimUimport jogl.util.GLIBimport javax.swing.JFrameimport common.util.anim.AnimCEimport common.util.anim.AnimU.UTypeimport com.jogamp.opengl.util.FPSAnimatorimport com.jogamp.opengl.GLEventListenerimport com.jogamp.opengl.GLAutoDrawableimport page.awt.BBBuilderimport page.battle.BattleBox.OuterBoximport common.battle.SBCtrlimport page.battle.BattleBoximport jogl.GLBattleBoximport common.battle.BattleFieldimport page.anim.IconBoximport jogl.GLIconBoximport jogl.GLBBRecdimport page.awt.RecdThreadimport page.view.ViewBoximport jogl.GLViewBoximport page.view.ViewBox.Controllerimport java.awt.AWTExceptionimport page.battle.BBRecdimport jogl.GLRecorderimport com.jogamp.opengl.GLProfileimport com.jogamp.opengl.GLCapabilitiesimport page.anim.IconBox.IBCtrlimport page.anim.IconBox.IBConfimport page.view.ViewBox.VBExporterimport jogl.GLRecdBImgimport page.JTGimport jogl.GLCstdimport jogl.GLVBExporterimport common.util.anim.EAnimIimport page.RetFuncimport page.battle.BattleBox.BBPainterimport page.battle.BBCtrlimport javax.swing.JOptionPaneimport kotlin.jvm.Strictfpimport main.Invimport javax.swing.SwingUtilitiesimport java.lang.InterruptedExceptionimport utilpc.UtilPC.PCItrimport utilpc.awt.PCIBimport jogl.GLBBBimport page.awt.AWTBBBimport utilpc.Themeimport page.MainPageimport common.io.assets.AssetLoaderimport common.pack.Source.Workspaceimport common.io.PackLoader.ZipDesc.FileDescimport common.io.assets.Adminimport page.awt.BattleBoxDefimport page.awt.IconBoxDefimport page.awt.BBRecdAWTimport page.awt.ViewBoxDefimport org.jcodec.api.awt.AWTSequenceEncoderimport page.awt.RecdThread.PNGThreadimport page.awt.RecdThread.MP4Threadimport page.awt.RecdThread.GIFThreadimport java.awt.GradientPaintimport utilpc.awt.FG2Dimport page.anim.TreeContimport javax.swing.JTreeimport javax.swing.event.TreeExpansionListenerimport common.util.anim.MaModelimport javax.swing.tree.DefaultMutableTreeNodeimport javax.swing.event.TreeExpansionEventimport java.util.function.IntPredicateimport javax.swing.tree.DefaultTreeModelimport common.util.anim.EAnimDimport page.anim.AnimBoximport utilpc.PPimport common.CommonStatic.BCAuxAssetsimport common.CommonStatic.EditLinkimport page.JBTNimport page.anim.DIYViewPageimport page.anim.ImgCutEditPageimport page.anim.MaModelEditPageimport page.anim.MaAnimEditPageimport page.anim.EditHeadimport java.awt.event.ActionListenerimport page.anim.AbEditPageimport common.util.anim.EAnimSimport page.anim.ModelBoximport common.util.anim.ImgCutimport page.view.AbViewPageimport javax.swing.JListimport javax.swing.JScrollPaneimport javax.swing.JComboBoximport utilpc.UtilPCimport javax.swing.event.ListSelectionListenerimport javax.swing.event.ListSelectionEventimport common.system.VImgimport page.support.AnimLCRimport page.support.AnimTableimport common.util.anim.MaAnimimport java.util.EventObjectimport javax.swing.text.JTextComponentimport page.anim.PartEditTableimport javax.swing.ListSelectionModelimport page.support.AnimTableTHimport page.JTFimport utilpc.ReColorimport page.anim.ImgCutEditTableimport page.anim.SpriteBoximport page.anim.SpriteEditPageimport java.awt.event.FocusAdapterimport java.awt.event.FocusEventimport common.pack.PackData.UserPackimport utilpc.Algorithm.SRResultimport page.anim.MaAnimEditTableimport javax.swing.JSliderimport java.awt.event.MouseWheelEventimport common.util.anim.EPartimport javax.swing.event.ChangeEventimport page.anim.AdvAnimEditPageimport javax.swing.BorderFactoryimport page.JLimport javax.swing.ImageIconimport page.anim.MMTreeimport javax.swing.event.TreeSelectionListenerimport javax.swing.event.TreeSelectionEventimport page.support.AbJTableimport page.anim.MaModelEditTableimport page.info.edit.ProcTableimport page.info.edit.ProcTable.AtkProcTableimport page.info.edit.SwingEditorimport page.info.edit.ProcTable.MainProcTableimport page.support.ListJtfPolicyimport page.info.edit.SwingEditor.SwingEGimport common.util.Data.Procimport java.lang.Runnableimport javax.swing.JComponentimport page.info.edit.LimitTableimport page.pack.CharaGroupPageimport page.pack.LvRestrictPageimport javax.swing.SwingConstantsimport common.util.lang.Editors.EditorGroupimport common.util.lang.Editors.EdiFieldimport common.util.lang.Editorsimport common.util.lang.ProcLangimport page.info.edit.EntityEditPageimport common.util.lang.Editors.EditorSupplierimport common.util.lang.Editors.EditControlimport page.info.edit.SwingEditor.IntEditorimport page.info.edit.SwingEditor.BoolEditorimport page.info.edit.SwingEditor.IdEditorimport page.SupPageimport common.util.unit.AbEnemyimport common.pack.IndexContainer.Indexableimport common.pack.Context.SupExcimport common.battle.data .AtkDataModelimport utilpc.Interpretimport common.battle.data .CustomEntityimport page.info.filter.UnitEditBoximport common.battle.data .CustomUnitimport common.util.stage.SCGroupimport page.info.edit.SCGroupEditTableimport common.util.stage.SCDefimport page.info.filter.EnemyEditBoximport common.battle.data .CustomEnemyimport page.info.StageFilterPageimport page.view.BGViewPageimport page.view.CastleViewPageimport page.view.MusicPageimport common.util.stage.CastleImgimport common.util.stage.CastleListimport java.text.DecimalFormatimport common.util.stage.Recdimport common.util.stage.MapColc.PackMapColcimport page.info.edit.StageEditTableimport page.support.ReorderListimport page.info.edit.HeadEditTableimport page.info.filter.EnemyFindPageimport page.battle.BattleSetupPageimport page.info.edit.AdvStEditPageimport page.battle.StRecdPageimport page.info.edit.LimitEditPageimport page.support.ReorderListenerimport common.util.pack.Soulimport page.info.edit.AtkEditTableimport page.info.filter.UnitFindPageimport common.battle.Basisimport common.util.Data.Proc.IMUimport javax.swing.DefaultComboBoxModelimport common.util.Animableimport common.util.pack.Soul.SoulTypeimport page.view.UnitViewPageimport page.view.EnemyViewPageimport page.info.edit.SwingEditor.EditCtrlimport page.support.Reorderableimport page.info.EnemyInfoPageimport common.util.unit.EneRandimport page.pack.EREditPageimport page.support.InTableTHimport page.support.EnemyTCRimport javax.swing.DefaultListCellRendererimport page.info.filter.UnitListTableimport page.info.filter.UnitFilterBoximport page.info.filter.EnemyListTableimport page.info.filter.EnemyFilterBoximport page.info.filter.UFBButtonimport page.info.filter.UFBListimport common.battle.data .MaskUnitimport javax.swing.AbstractButtonimport page.support.SortTableimport page.info.UnitInfoPageimport page.support.UnitTCRimport page.info.filter.EFBButtonimport page.info.filter.EFBListimport common.util.stage.LvRestrictimport common.util.stage.CharaGroupimport page.info.StageTableimport page.info.TreaTableimport javax.swing.JPanelimport page.info.UnitInfoTableimport page.basis.BasisPageimport kotlin.jvm.JvmOverloadsimport page.info.EnemyInfoTableimport common.util.stage.RandStageimport page.info.StagePageimport page.info.StageRandPageimport common.util.unit.EFormimport page.pack.EREditTableimport common.util.EREntimport common.pack.FixIndexListimport page.support.UnitLCRimport page.pack.RecdPackPageimport page.pack.CastleEditPageimport page.pack.BGEditPageimport page.pack.CGLREditPageimport common.pack.Source.ZipSourceimport page.info.edit.EnemyEditPageimport page.info.edit.StageEditPageimport page.info.StageViewPageimport page.pack.UnitManagePageimport page.pack.MusicEditPageimport page.battle.AbRecdPageimport common.system.files.VFileRootimport java.awt.Desktopimport common.pack.PackDataimport common.util.unit.UnitLevelimport page.info.edit.FormEditPageimport common.util.anim.AnimIimport common.util.anim.AnimI.AnimTypeimport common.util.anim.AnimDimport common.battle.data .Orbimport page.basis.LineUpBoximport page.basis.LubContimport common.battle.BasisLUimport page.basis.ComboListTableimport page.basis.ComboListimport page.basis.NyCasBoximport page.basis.UnitFLUPageimport common.util.unit.Comboimport page.basis.LevelEditPageimport common.util.pack.NyCastleimport common.battle.LineUpimport common.system.SymCoordimport java.util.TreeSetimport page.basis.OrbBoximport javax.swing.table.DefaultTableCellRendererimport javax.swing.JTableimport common.CommonStatic.BattleConstimport common.battle.StageBasisimport common.util.ImgCoreimport common.battle.attack.ContAbimport common.battle.entity.EAnimContimport common.battle.entity.WaprContimport page.battle.RecdManagePageimport page.battle.ComingTableimport common.util.stage.EStageimport page.battle.EntityTableimport common.battle.data .MaskEnemyimport common.battle.SBRplyimport common.battle.entity.AbEntityimport page.battle.RecdSavePageimport page.LocCompimport page.LocSubCompimport javax.swing.table.TableModelimport page.support.TModelimport javax.swing.event.TableModelListenerimport javax.swing.table.DefaultTableColumnModelimport javax.swing.JFileChooserimport javax.swing.filechooser.FileNameExtensionFilterimport javax.swing.TransferHandlerimport java.awt.datatransfer.Transferableimport java.awt.datatransfer.DataFlavorimport javax.swing.DropModeimport javax.swing.TransferHandler.TransferSupportimport java.awt.dnd.DragSourceimport java.awt.datatransfer.UnsupportedFlavorExceptionimport common.system.Copableimport page.support.AnimTransferimport javax.swing.DefaultListModelimport page.support.InListTHimport java.awt.FocusTraversalPolicyimport javax.swing.JTextFieldimport page.CustomCompimport javax.swing.JToggleButtonimport javax.swing.JButtonimport javax.swing.ToolTipManagerimport javax.swing.JRootPaneimport javax.swing.JProgressBarimport page.ConfigPageimport page.view.EffectViewPageimport page.pack.PackEditPageimport page.pack.ResourcePageimport javax.swing.WindowConstantsimport java.awt.event.AWTEventListenerimport java.awt.AWTEventimport java.awt.event.ComponentAdapterimport java.awt.event.ComponentEventimport java.util.ConcurrentModificationExceptionimport javax.swing.plaf.FontUIResourceimport java.util.Enumerationimport javax.swing.UIManagerimport common.CommonStatic.FakeKeyimport page.LocSubComp.LocBinderimport page.LSCPopimport java.awt.BorderLayoutimport java.awt.GridLayoutimport javax.swing.JTextPaneimport page.TTTimport java.util.ResourceBundleimport java.util.MissingResourceExceptionimport java.util.Localeimport common.io.json.Test.JsonTest_2import common.pack.PackData.PackDescimport common.io.PackLoaderimport common.io.PackLoader.Preloadimport common.io.PackLoader.ZipDescimport common.io.json.Testimport common.io.json.JsonClassimport common.io.json.JsonFieldimport common.io.json.JsonField.GenTypeimport common.io.json.Test.JsonTest_0.JsonDimport common.io.json.JsonClass.RTypeimport java.util.HashSetimport common.io.json.JsonDecoder.OnInjectedimport common.io.json.JsonField.IOTypeimport common.io.json.JsonExceptionimport common.io.json.JsonClass.NoTagimport common.io.json.JsonField.SerTypeimport common.io.json.JsonClass.WTypeimport kotlin.reflect.KClassimport com.google.gson.JsonArrayimport common.io.assets.Admin.StaticPermittedimport common.io.json.JsonClass.JCGenericimport common.io.json.JsonClass.JCGetterimport com.google.gson.JsonPrimitiveimport com.google.gson.JsonNullimport common.io.json.JsonClass.JCIdentifierimport java.lang.ClassNotFoundExceptionimport common.io.assets.AssetLoader.AssetHeaderimport common.io.assets.AssetLoader.AssetHeader.AssetEntryimport common.io.InStreamDefimport common.io.BCUExceptionimport java.io.UnsupportedEncodingExceptionimport common.io.OutStreamDefimport javax.crypto.Cipherimport javax.crypto.spec.IvParameterSpecimport javax.crypto.spec.SecretKeySpecimport common.io.PackLoader.FileSaverimport common.system.files.FDByteimport common.io.json.JsonClass.JCConstructorimport common.io.PackLoader.FileLoader.FLStreamimport common.io.PackLoader.PatchFileimport java.lang.NullPointerExceptionimport java.lang.IndexOutOfBoundsExceptionimport common.io.MultiStreamimport java.io.RandomAccessFileimport common.io.MultiStream.TrueStreamimport java.lang.RuntimeExceptionimport common.pack.Source.ResourceLocationimport common.pack.Source.AnimLoaderimport common.pack.Source.SourceAnimLoaderimport common.util.anim.AnimCIimport common.system.files.FDFileimport common.pack.IndexContainerimport common.battle.data .PCoinimport common.util.pack.EffAnimimport common.battle.data .DataEnemyimport common.util.stage.Limit.DefLimitimport common.pack.IndexContainer.Reductorimport common.pack.FixIndexList.FixIndexMapimport common.pack.VerFixer.IdFixerimport common.pack.IndexContainer.IndexContimport common.pack.IndexContainer.ContGetterimport common.util.stage.CastleList.PackCasListimport common.util.Data.Proc.THEMEimport common.CommonStatic.ImgReaderimport common.pack.VerFixerimport common.pack.VerFixer.VerFixerExceptionimport java.lang.NumberFormatExceptionimport common.pack.Source.SourceAnimSaverimport common.pack.VerFixer.EnemyFixerimport common.pack.VerFixer.PackFixerimport common.pack.PackData.DefPackimport java.util.function.BiConsumerimport common.util.BattleStaticimport common.util.anim.AnimU.ImageKeeperimport common.util.anim.AnimCE.AnimCELoaderimport common.util.anim.AnimCI.AnimCIKeeperimport common.util.anim.AnimUD.DefImgLoaderimport common.util.BattleObjimport common.util.Data.Proc.ProcItemimport common.util.lang.ProcLang.ItemLangimport common.util.lang.LocaleCenter.Displayableimport common.util.lang.Editors.DispItemimport common.util.lang.LocaleCenter.ObjBinderimport common.util.lang.LocaleCenter.ObjBinder.BinderFuncimport common.util.Data.Proc.PROBimport org.jcodec.common.tools.MathUtilimport common.util.Data.Proc.PTimport common.util.Data.Proc.PTDimport common.util.Data.Proc.PMimport common.util.Data.Proc.WAVEimport common.util.Data.Proc.WEAKimport common.util.Data.Proc.STRONGimport common.util.Data.Proc.BURROWimport common.util.Data.Proc.REVIVEimport common.util.Data.Proc.SUMMONimport common.util.Data.Proc.MOVEWAVEimport common.util.Data.Proc.POISONimport common.util.Data.Proc.CRITIimport common.util.Data.Proc.VOLCimport common.util.Data.Proc.ARMORimport common.util.Data.Proc.SPEEDimport java.util.LinkedHashMapimport common.util.lang.LocaleCenter.DisplayItemimport common.util.lang.ProcLang.ProcLangStoreimport common.util.lang.Formatter.IntExpimport common.util.lang.Formatter.RefObjimport common.util.lang.Formatter.BoolExpimport common.util.lang.Formatter.BoolElemimport common.util.lang.Formatter.IElemimport common.util.lang.Formatter.Contimport common.util.lang.Formatter.Elemimport common.util.lang.Formatter.RefElemimport common.util.lang.Formatter.RefFieldimport common.util.lang.Formatter.RefFuncimport common.util.lang.Formatter.TextRefimport common.util.lang.Formatter.CodeBlockimport common.util.lang.Formatter.TextPlainimport common.util.unit.Unit.UnitInfoimport common.util.lang.MultiLangCont.MultiLangStaticsimport common.util.pack.EffAnim.EffTypeimport common.util.pack.EffAnim.ArmorEffimport common.util.pack.EffAnim.BarEneEffimport common.util.pack.EffAnim.BarrierEffimport common.util.pack.EffAnim.DefEffimport common.util.pack.EffAnim.WarpEffimport common.util.pack.EffAnim.ZombieEffimport common.util.pack.EffAnim.KBEffimport common.util.pack.EffAnim.SniperEffimport common.util.pack.EffAnim.VolcEffimport common.util.pack.EffAnim.SpeedEffimport common.util.pack.EffAnim.WeakUpEffimport common.util.pack.EffAnim.EffAnimStoreimport common.util.pack.NyCastle.NyTypeimport common.util.pack.WaveAnimimport common.util.pack.WaveAnim.WaveTypeimport common.util.pack.Background.BGWvTypeimport common.util.unit.Form.FormJsonimport common.system.BasedCopableimport common.util.anim.AnimUDimport common.battle.data .DataUnitimport common.battle.entity.EUnitimport common.battle.entity.EEnemyimport common.util.EntRandimport common.util.stage.Recd.Waitimport java.lang.CloneNotSupportedExceptionimport common.util.stage.StageMap.StageMapInfoimport common.util.stage.Stage.StageInfoimport common.util.stage.Limit.PackLimitimport common.util.stage.MapColc.ClipMapColcimport common.util.stage.CastleList.DefCasListimport common.util.stage.MapColc.StItrimport common.util.Data.Proc.IntType.BitCountimport common.util.CopRandimport common.util.LockGLimport java.lang.IllegalAccessExceptionimport common.battle.data .MaskAtkimport common.battle.data .DefaultDataimport common.battle.data .DataAtkimport common.battle.data .MaskEntityimport common.battle.data .DataEntityimport common.battle.attack.AtkModelAbimport common.battle.attack.AttackAbimport common.battle.attack.AttackSimpleimport common.battle.attack.AttackWaveimport common.battle.entity.Cannonimport common.battle.attack.AttackVolcanoimport common.battle.attack.ContWaveAbimport common.battle.attack.ContWaveDefimport common.battle.attack.AtkModelEntityimport common.battle.entity.EntContimport common.battle.attack.ContMoveimport common.battle.attack.ContVolcanoimport common.battle.attack.ContWaveCanonimport common.battle.attack.AtkModelEnemyimport common.battle.attack.AtkModelUnitimport common.battle.attack.AttackCanonimport common.battle.entity.EUnit.OrbHandlerimport common.battle.entity.Entity.AnimManagerimport common.battle.entity.Entity.AtkManagerimport common.battle.entity.Entity.ZombXimport common.battle.entity.Entity.KBManagerimport common.battle.entity.Entity.PoisonTokenimport common.battle.entity.Entity.WeakTokenimport common.battle.Treasureimport common.battle.MirrorSetimport common.battle.Releaseimport common.battle.ELineUpimport common.battle.entity.Sniperimport common.battle.entity.ECastleimport java.util.Dequeimport common.CommonStatic.Itfimport java.lang.Characterimport common.CommonStatic.ImgWriterimport utilpc.awt.FTATimport utilpc.awt.Blenderimport java.awt.RenderingHintsimport utilpc.awt.BIBuilderimport java.awt.CompositeContextimport java.awt.image.Rasterimport java.awt.image.WritableRasterimport utilpc.ColorSetimport utilpc.OggTimeReaderimport utilpc.UtilPC.PCItr.MusicReaderimport utilpc.UtilPC.PCItr.PCALimport javax.swing.UIManager.LookAndFeelInfoimport java.lang.InstantiationExceptionimport javax.swing.UnsupportedLookAndFeelExceptionimport utilpc.Algorithm.ColorShiftimport utilpc.Algorithm.StackRect
class JsonDecoder private constructor(parent: JsonDecoder?, json: JsonObject, cls: Class<*>, pre: Any) {
    interface Decoder {
        @Throws(Exception::class)
        fun decode(elem: JsonElement?): Any?
    }

    @Documented
    @Retention(RetentionPolicy.RUNTIME)
    @Target(AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY_GETTER, AnnotationTarget.PROPERTY_SETTER)
    annotation class OnInjected
    companion object {
        @StaticPermitted
        val REGISTER: MutableMap<Class<*>, common.io.json.JsonDecoder.Decoder> = HashMap()

        @StaticPermitted(Admin.StaticPermitted.Type.TEMP)
        private var current: JsonDecoder
        @Throws(Exception::class)
        fun decode(elem: JsonElement, cls: Class<*>, par: JsonDecoder?): Any? {
            if (elem.isJsonNull()) return null
            if (JsonElement::class.java.isAssignableFrom(cls)) return elem
            val dec = REGISTER[cls]
            if (dec != null) return dec.decode(elem)
            if (cls.isArray) return decodeArray(elem, cls, par)
            if (MutableList::class.java.isAssignableFrom(cls)) return decodeList(elem, cls, par)
            if (MutableMap::class.java.isAssignableFrom(cls)) return decodeMap(elem, cls, par)
            if (MutableSet::class.java.isAssignableFrom(cls)) return decodeSet(elem, cls, par)
            // alias
            if (cls.getAnnotation(JCGeneric::class.java) != null && par != null && par.curjfld!!.alias().size > par.index) {
                val jcg: JCGeneric = cls.getAnnotation(JCGeneric::class.java)
                val alias: Class<*> = par.curjfld!!.alias().get(par.index)
                var found = false
                for (ala in jcg.value()) if (ala == alias) {
                    found = true
                    break
                }
                if (!found) throw JsonException(JsonException.Type.TYPE_MISMATCH, null, "class not present in JCGeneric")
                val input = decode(elem, alias, par)
                for (m in alias.declaredMethods) if (m.getAnnotation(JCGetter::class.java) != null) return m.invoke(input)
                throw JsonException(JsonException.Type.TYPE_MISMATCH, null, "no JCGenericRead present")
            }
            // fill existing object
            if (par != null && par.curjfld!!.gen() == GenType.FILL) {
                val `val` = par.curfld!![par.obj]
                return if (cls.getAnnotation(JsonClass::class.java) != null) inject(par, elem.getAsJsonObject(), cls, `val`) else `val`
            }
            // generator
            if (par != null && par.curjfld!!.gen() == GenType.GEN) {
                val ccls: Class<*> = par.obj!!.javaClass
                // default generator
                if (par.curjfld!!.generator().length == 0) {
                    var cst: Constructor<*>? = null
                    for (ci in cls.declaredConstructors) if (ci.parameterCount == 1 && ci.parameters[0].type.isAssignableFrom(ccls)) cst = ci
                    if (cst == null) throw JsonException(JsonException.Type.FUNC, null, "no constructor found: $cls")
                    val `val` = cst.newInstance(par.obj)
                    return inject(par, elem.getAsJsonObject(), cls, `val`)
                }
                // functional generator
                val m = ccls.getMethod(par.curjfld!!.generator(), Class::class.java, JsonElement::class.java)
                return m.invoke(par.obj, cls, elem)
            }
            if (cls.getAnnotation(JsonClass::class.java) != null) return decodeObject(elem, cls, par)
            throw JsonException(JsonException.Type.UNDEFINED, elem, "class not possible to generate")
        }

        fun <T> decode(elem: JsonElement, cls: Class<T>): T {
            return Data.Companion.err<Any>(SupExc<Any> { decode(elem, cls, null) })
        }

        @Throws(JsonException::class)
        fun getBoolean(elem: JsonElement): Boolean {
            if (!elem.isJsonPrimitive() || !(elem as JsonPrimitive).isBoolean()) throw JsonException(JsonException.Type.TYPE_MISMATCH, elem, "this element is not boolean")
            return elem.getAsBoolean()
        }

        @Throws(JsonException::class)
        fun getByte(elem: JsonElement): Byte {
            if (!elem.isJsonPrimitive() || !(elem as JsonPrimitive).isNumber()) throw JsonException(JsonException.Type.TYPE_MISMATCH, elem, "this element is not number")
            return elem.getAsByte()
        }

        @Throws(JsonException::class)
        fun getDouble(elem: JsonElement): Double {
            if (!elem.isJsonPrimitive() || !(elem as JsonPrimitive).isNumber()) throw JsonException(JsonException.Type.TYPE_MISMATCH, elem, "this element is not number")
            return elem.getAsDouble()
        }

        @Throws(JsonException::class)
        fun getFloat(elem: JsonElement): Float {
            if (!elem.isJsonPrimitive() || !(elem as JsonPrimitive).isNumber()) throw JsonException(JsonException.Type.TYPE_MISMATCH, elem, "this element is not number")
            return elem.getAsFloat()
        }

        fun <T> getGlobal(cls: Class<T>): T? {
            return getGlobal(current, cls) as T?
        }

        @Throws(JsonException::class)
        fun getInt(elem: JsonElement): Int {
            if (!elem.isJsonPrimitive() || !(elem as JsonPrimitive).isNumber()) throw JsonException(JsonException.Type.TYPE_MISMATCH, elem, "this element is not number")
            return elem.getAsInt()
        }

        @Throws(JsonException::class)
        fun getLong(elem: JsonElement): Long {
            if (!elem.isJsonPrimitive() || !(elem as JsonPrimitive).isNumber()) throw JsonException(JsonException.Type.TYPE_MISMATCH, elem, "this element is not number")
            return elem.getAsLong()
        }

        @Throws(JsonException::class)
        fun getShort(elem: JsonElement): Short {
            if (!elem.isJsonPrimitive() || !(elem as JsonPrimitive).isNumber()) throw JsonException(JsonException.Type.TYPE_MISMATCH, elem, "this element is not number")
            return elem.getAsShort()
        }

        @Throws(JsonException::class)
        fun getString(elem: JsonElement): String? {
            if (elem.isJsonNull()) return null
            if (elem.isJsonArray()) {
                var ans = ""
                val arr: JsonArray = elem.getAsJsonArray()
                for (i in 0 until arr.size()) ans += arr.get(i).getAsString()
                return ans
            }
            if (!elem.isJsonPrimitive() || !(elem as JsonPrimitive).isString()) throw JsonException(JsonException.Type.TYPE_MISMATCH, elem, "this element is not string")
            return elem.getAsString()
        }

        @Throws(Exception::class)
        fun <T> inject(elem: JsonElement, cls: Class<T>, pre: T): T? {
            return inject(null, elem.getAsJsonObject(), cls, pre) as T?
        }

        @Throws(Exception::class)
        protected fun decodeList(elem: JsonElement, cls: Class<*>, par: JsonDecoder?): List<Any?>? {
            if (par!!.curjfld == null || par.curjfld.generic().size != 1) throw JsonException(JsonException.Type.TAG, null, "generic data structure requires typeProvider tag")
            if (elem.isJsonNull()) return null
            val `val` = cls.newInstance() as MutableList<Any?>
            if (elem.isJsonObject() && par.curjfld.usePool()) {
                val pool: JsonArray = elem.getAsJsonObject().get("pool").getAsJsonArray()
                val data: JsonArray = elem.getAsJsonObject().get("data").getAsJsonArray()
                val handler = JsonField.Handler(pool, null, par)
                val n: Int = data.size()
                for (i in 0 until n) `val`.add(handler[data.get(i).getAsInt()])
                return `val`
            }
            if (!elem.isJsonArray()) throw JsonException(JsonException.Type.TYPE_MISMATCH, elem, "this element is not array")
            val jarr: JsonArray = elem.getAsJsonArray()
            val n: Int = jarr.size()
            for (i in 0 until n) {
                `val`.add(decode(jarr.get(i), par.curjfld.generic().get(0), par))
            }
            return `val`
        }

        @Throws(Exception::class)
        private fun decodeArray(elem: JsonElement, cls: Class<*>, par: JsonDecoder?): Any {
            val ccls = cls.componentType
            val jf: JsonField? = par?.curjfld
            if (elem.isJsonObject() && jf != null && jf.usePool()) {
                val pool: JsonArray = elem.getAsJsonObject().get("pool").getAsJsonArray()
                val data: JsonArray = elem.getAsJsonObject().get("data").getAsJsonArray()
                val handler = JsonField.Handler(pool, ccls, par)
                val n: Int = data.size()
                val arr = getArray(ccls, n, par)
                for (i in 0 until n) Array.set(arr, i, handler[data.get(i).getAsInt()])
                return arr
            }
            if (!elem.isJsonArray()) throw JsonException(JsonException.Type.TYPE_MISMATCH, elem, "this element is not array")
            val jarr: JsonArray = elem.getAsJsonArray()
            val n: Int = jarr.size()
            val arr = getArray(ccls, n, par)
            for (i in 0 until n) Array.set(arr, i, decode(jarr.get(i), ccls, par))
            return arr
        }

        @Throws(Exception::class)
        private fun decodeMap(elem: JsonElement, cls: Class<*>, par: JsonDecoder?): Map<Any?, Any?>? {
            if (par!!.curjfld == null || par.curjfld.generic().size != 2) throw JsonException(JsonException.Type.TAG, null, "generic data structure requires typeProvider tag")
            if (elem.isJsonNull()) return null
            if (!elem.isJsonArray()) throw JsonException(JsonException.Type.TYPE_MISMATCH, elem, "this element is not array")
            val jarr: JsonArray = elem.getAsJsonArray()
            val n: Int = jarr.size()
            val `val` = cls.newInstance() as MutableMap<Any?, Any?>
            for (i in 0 until n) {
                val obj: JsonObject = jarr.get(i).getAsJsonObject()
                val key = decode(obj.get("key"), par.curjfld.generic().get(0), par)
                par.index = 1
                val ent = decode(obj.get("val"), par.curjfld.generic().get(1), par)
                par.index = 0
                `val`[key] = ent
            }
            return `val`
        }

        @Throws(Exception::class)
        private fun decodeObject(elem: JsonElement, cls: Class<*>, par: JsonDecoder?): Any? {
            if (elem.isJsonNull()) return null
            if (!elem.isJsonObject()) throw JsonException(JsonException.Type.TYPE_MISMATCH, elem, "this element is not object for $cls")
            val jobj: JsonObject = elem.getAsJsonObject()
            val jc: JsonClass = cls.getAnnotation(JsonClass::class.java)
            return if (jc.read() == RType.FILL) throw JsonException(JsonException.Type.FUNC, null, "RType FILL requires GenType FILL or GEN") else if (jc.read() == RType.DATA) inject(par, jobj, cls, null) else if (jc.read() == RType.MANUAL) {
                val func: String = jc.generator()
                if (func.length == 0) throw JsonException(JsonException.Type.FUNC, elem, "no generate function")
                val m = cls.getMethod(func, JsonElement::class.java)
                m.invoke(null, jobj)
            } else throw JsonException(JsonException.Type.UNDEFINED, elem, "class not possible to generate")
        }

        @Throws(Exception::class)
        private fun decodeSet(elem: JsonElement, cls: Class<*>, par: JsonDecoder?): Set<Any?>? {
            if (par!!.curjfld == null || par.curjfld.generic().size != 1) throw JsonException(JsonException.Type.TAG, null, "generic data structure requires typeProvider tag")
            if (elem.isJsonNull()) return null
            if (!elem.isJsonArray()) throw JsonException(JsonException.Type.TYPE_MISMATCH, elem, "this element is not array")
            val jarr: JsonArray = elem.getAsJsonArray()
            val n: Int = jarr.size()
            val `val` = cls.newInstance() as MutableSet<Any?>
            for (i in 0 until n) {
                `val`.add(decode(jarr.get(i), par.curjfld.generic().get(0), par))
            }
            return `val`
        }

        @Throws(Exception::class)
        private fun getArray(cls: Class<*>, n: Int, par: JsonDecoder?): Any {
            return if (par != null && par.curjfld != null && par.curjfld.gen() == GenType.FILL) {
                if (par.curfld == null || par.obj == null) throw JsonException(JsonException.Type.TAG, null, "no enclosing object")
                par.curfld!![par.obj]
            } else Array.newInstance(cls, n)
        }

        private fun getGlobal(par: JsonDecoder, cls: Class<*>): Any? {
            var dec = par
            while (dec != null) {
                if (cls.isInstance(dec.obj)) return dec.obj
                dec = dec.par
            }
            return null
        }

        @Throws(Exception::class)
        private fun inject(par: JsonDecoder?, jobj: JsonObject, cls: Class<*>, pre: Any?): Any? {
            return JsonDecoder(par, jobj, cls, pre ?: cls.newInstance()).obj
        }

        init {
            REGISTER[java.lang.Boolean.TYPE] = common.io.json.JsonDecoder.Decoder { elem: JsonElement? -> getBoolean(common.io.json.elem) }
            REGISTER[Boolean::class.java] = common.io.json.JsonDecoder.Decoder { elem: JsonElement? -> getBoolean(common.io.json.elem) }
            REGISTER[java.lang.Byte.TYPE] = common.io.json.JsonDecoder.Decoder { elem: JsonElement? -> getByte(common.io.json.elem) }
            REGISTER[Byte::class.java] = common.io.json.JsonDecoder.Decoder { elem: JsonElement? -> getByte(common.io.json.elem) }
            REGISTER[java.lang.Short.TYPE] = common.io.json.JsonDecoder.Decoder { elem: JsonElement? -> getShort(common.io.json.elem) }
            REGISTER[Short::class.java] = common.io.json.JsonDecoder.Decoder { elem: JsonElement? -> getShort(common.io.json.elem) }
            REGISTER[Integer.TYPE] = common.io.json.JsonDecoder.Decoder { elem: JsonElement? -> getInt(common.io.json.elem) }
            REGISTER[Int::class.java] = common.io.json.JsonDecoder.Decoder { elem: JsonElement? -> getInt(common.io.json.elem) }
            REGISTER[java.lang.Long.TYPE] = common.io.json.JsonDecoder.Decoder { elem: JsonElement? -> getLong(common.io.json.elem) }
            REGISTER[Long::class.java] = common.io.json.JsonDecoder.Decoder { elem: JsonElement? -> getLong(common.io.json.elem) }
            REGISTER[java.lang.Float.TYPE] = common.io.json.JsonDecoder.Decoder { elem: JsonElement? -> getFloat(common.io.json.elem) }
            REGISTER[Float::class.java] = common.io.json.JsonDecoder.Decoder { elem: JsonElement? -> getFloat(common.io.json.elem) }
            REGISTER[java.lang.Double.TYPE] = common.io.json.JsonDecoder.Decoder { elem: JsonElement? -> getDouble(common.io.json.elem) }
            REGISTER[Double::class.java] = common.io.json.JsonDecoder.Decoder { elem: JsonElement? -> getDouble(common.io.json.elem) }
            REGISTER[String::class.java] = common.io.json.JsonDecoder.Decoder { elem: JsonElement? -> getString(common.io.json.elem) }
            REGISTER[Class::class.java] = common.io.json.JsonDecoder.Decoder { elem: JsonElement? -> Class.forName(getString(common.io.json.elem)) }
        }
    }

    private val par: JsonDecoder
    private val jobj: JsonObject
    private val obj: Any?
    private val tarcls: Class<*>
    private val tarjcls: JsonClass
    private var curcls: Class<*>? = null
    private var curjcls: JsonClass? = null
    private var curfld: Field? = null
    var curjfld: JsonField? = null
    private var index = 0
    @Throws(Exception::class)
    private fun decode(cls: Class<*>) {
        if (cls.superclass.getAnnotation(JsonClass::class.java) != null) decode(cls.superclass)
        curcls = cls
        curjcls = cls.getAnnotation(JsonClass::class.java)
        if (curjcls == null) throw JsonException(JsonException.Type.TYPE_MISMATCH, jobj, "no annotation for class $curcls")
        val fs = cls.declaredFields
        for (f in fs) {
            if (Modifier.isStatic(f.modifiers)) continue
            curjfld = f.getAnnotation(JsonField::class.java)
            if (curjfld == null && curjcls.noTag() == NoTag.LOAD) curjfld = JsonField.Companion.DEF
            if (curjfld == null || curjfld.block() || curjfld.io() == IOType.W) continue
            var tag: String = curjfld.tag()
            if (tag.length == 0) tag = f.name
            if (!jobj.has(tag)) continue
            val elem: JsonElement = jobj.get(tag)
            f.isAccessible = true
            curfld = f
            f[obj] = decode(elem, f.type, getInvoker())
            curfld = null
        }
        var oni: Method? = null
        for (m in cls.declaredMethods) {
            if (m.getAnnotation(OnInjected::class.java) != null) oni = if (oni == null) m else throw JsonException(JsonException.Type.FUNC, null, "duplicate OnInjected")
            curjfld = m.getAnnotation(JsonField::class.java)
            if (curjfld == null || curjfld.io() == IOType.W) continue
            if (curjfld.io() == IOType.RW) throw JsonException(JsonException.Type.FUNC, null, "functional fields should not have RW type")
            if (m.parameterCount != 1) throw JsonException(JsonException.Type.FUNC, null, "parameter count should be 1")
            val tag: String = curjfld.tag()
            if (tag.length == 0) throw JsonException(JsonException.Type.TAG, null, "function fields must have tag")
            if (!jobj.has(tag)) continue
            val elem: JsonElement = jobj.get(tag)
            val ccls = m.parameters[0].type
            m.invoke(obj, decode(elem, ccls, getInvoker()))
        }
        oni?.invoke(obj)
    }

    private fun getInvoker(): JsonDecoder {
        return if (tarjcls.bypass()) par else this
    }

    init {
        par = parent ?: current
        jobj = json
        obj = pre
        tarcls = cls
        tarjcls = cls.getAnnotation(JsonClass::class.java)
        current = getInvoker()
        decode(tarcls)
        current = par
    }
}