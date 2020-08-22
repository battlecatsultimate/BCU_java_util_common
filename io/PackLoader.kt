package common.ioimport

import com.google.gson.JsonParser
import common.io.MultiStream
import common.system.files.FileData
import common.util.Data
import java.io.File
import java.io.InputStream
import java.io.OutputStream
import java.util.*

com.google.api.client.json.jackson2.JacksonFactoryimport com.google.api.services.drive.DriveScopesimport com.google.api.client.util.store.FileDataStoreFactoryimport com.google.api.client.http.HttpTransportimport com.google.api.services.drive.Driveimport kotlin.Throwsimport java.io.IOExceptionimport io.drive.DriveUtilimport java.io.FileNotFoundExceptionimport java.io.FileInputStreamimport com.google.api.client.googleapis.auth.oauth2.GoogleClientSecretsimport java.io.InputStreamReaderimport com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlowimport com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledAppimport com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiverimport com.google.api.client.googleapis.javanet.GoogleNetHttpTransportimport kotlin.jvm.JvmStaticimport io.drive.DrvieInitimport com.google.api.client.http.javanet.NetHttpTransportimport com.google.api.services.drive.model.FileListimport java.io.BufferedInputStreamimport java.io.FileOutputStreamimport com.google.api.client.googleapis.media.MediaHttpDownloaderimport io.WebFileIOimport io.BCJSONimport page.LoadPageimport org.json.JSONObjectimport org.json.JSONArrayimport main.MainBCUimport main.Optsimport common.CommonStaticimport java.util.TreeMapimport java.util.Arraysimport java.io.BufferedReaderimport io.BCMusicimport common.util.stage.Musicimport io.BCPlayerimport java.util.HashMapimport javax.sound.sampled.Clipimport java.io.ByteArrayInputStreamimport javax.sound.sampled.AudioInputStreamimport javax.sound.sampled.AudioSystemimport javax.sound.sampled.DataLineimport javax.sound.sampled.FloatControlimport javax.sound.sampled.LineEventimport com.google.api.client.googleapis.media.MediaHttpDownloaderProgressListenerimport com.google.api.client.googleapis.media.MediaHttpDownloader.DownloadStateimport common.io.DataIOimport io.BCUReaderimport common.io.InStreamimport com.google.gson.JsonElementimport common.io.json.JsonDecoderimport com.google.gson.JsonObjectimport page.MainFrameimport page.view.ViewBox.Confimport page.MainLocaleimport page.battle.BattleInfoPageimport page.support.Exporterimport page.support.Importerimport common.pack.Context.ErrTypeimport common.util.stage.MapColcimport common.util.stage.MapColc.DefMapColcimport common.util.lang.MultiLangContimport common.util.stage.StageMapimport common.util.unit.Enemyimport io.BCUWriterimport java.text.SimpleDateFormatimport java.io.PrintStreamimport common.io.OutStreamimport common.battle.BasisSetimport res.AnimatedGifEncoderimport java.awt.image.BufferedImageimport javax.imageio.ImageIOimport java.security.MessageDigestimport java.security.NoSuchAlgorithmExceptionimport common.io.json.JsonEncoderimport java.io.FileWriterimport com.google.api.client.http.GenericUrlimport org.apache.http.impl .client.CloseableHttpClientimport org.apache.http.impl .client.HttpClientsimport org.apache.http.client.methods.HttpPostimport org.apache.http.entity.mime.content.FileBodyimport org.apache.http.entity.mime.MultipartEntityBuilderimport org.apache.http.entity.mime.HttpMultipartModeimport org.apache.http.HttpEntityimport org.apache.http.util.EntityUtilsimport com.google.api.client.http.HttpRequestInitializerimport com.google.api.client.http.HttpBackOffUnsuccessfulResponseHandlerimport com.google.api.client.util.ExponentialBackOffimport com.google.api.client.http.HttpBackOffIOExceptionHandlerimport res.NeuQuantimport res.LZWEncoderimport java.io.BufferedOutputStreamimport java.awt.Graphics2Dimport java.awt.image.DataBufferByteimport common.system.fake.FakeImageimport utilpc.awt.FIBIimport jogl.util.AmbImageimport common.system.files.VFileimport jogl.util.GLImageimport com.jogamp.opengl.util.texture.TextureDataimport common.system.Pimport com.jogamp.opengl.util.texture.TextureIOimport jogl.GLStaticimport com.jogamp.opengl.util.texture.awt.AWTTextureIOimport java.awt.AlphaCompositeimport common.system.fake.FakeImage.Markerimport jogl.util.GLGraphicsimport com.jogamp.opengl.GL2import jogl.util.GeoAutoimport com.jogamp.opengl.GL2ES3import com.jogamp.opengl.GLimport common.system.fake.FakeGraphicsimport common.system.fake.FakeTransformimport jogl.util.ResManagerimport jogl.util.GLGraphics.GeomGimport jogl.util.GLGraphics.GLCimport jogl.util.GLGraphics.GLTimport com.jogamp.opengl.GL2ES2import com.jogamp.opengl.util.glsl.ShaderCodeimport com.jogamp.opengl.util.glsl.ShaderProgramimport com.jogamp.opengl.GLExceptionimport jogl.StdGLCimport jogl.Tempimport common.util.anim.AnimUimport common.util.anim.EAnimUimport jogl.util.GLIBimport javax.swing.JFrameimport common.util.anim.AnimCEimport common.util.anim.AnimU.UTypeimport com.jogamp.opengl.util.FPSAnimatorimport com.jogamp.opengl.GLEventListenerimport com.jogamp.opengl.GLAutoDrawableimport page.awt.BBBuilderimport page.battle.BattleBox.OuterBoximport common.battle.SBCtrlimport page.battle.BattleBoximport jogl.GLBattleBoximport common.battle.BattleFieldimport page.anim.IconBoximport jogl.GLIconBoximport jogl.GLBBRecdimport page.awt.RecdThreadimport page.view.ViewBoximport jogl.GLViewBoximport page.view.ViewBox.Controllerimport java.awt.AWTExceptionimport page.battle.BBRecdimport jogl.GLRecorderimport com.jogamp.opengl.GLProfileimport com.jogamp.opengl.GLCapabilitiesimport page.anim.IconBox.IBCtrlimport page.anim.IconBox.IBConfimport page.view.ViewBox.VBExporterimport jogl.GLRecdBImgimport page.JTGimport jogl.GLCstdimport jogl.GLVBExporterimport common.util.anim.EAnimIimport page.RetFuncimport page.battle.BattleBox.BBPainterimport page.battle.BBCtrlimport javax.swing.JOptionPaneimport kotlin.jvm.Strictfpimport main.Invimport javax.swing.SwingUtilitiesimport java.lang.InterruptedExceptionimport utilpc.UtilPC.PCItrimport utilpc.awt.PCIBimport jogl.GLBBBimport page.awt.AWTBBBimport utilpc.Themeimport page.MainPageimport common.io.assets.AssetLoaderimport common.pack.Source.Workspaceimport common.io.PackLoader.ZipDesc.FileDescimport common.io.assets.Adminimport page.awt.BattleBoxDefimport page.awt.IconBoxDefimport page.awt.BBRecdAWTimport page.awt.ViewBoxDefimport org.jcodec.api.awt.AWTSequenceEncoderimport page.awt.RecdThread.PNGThreadimport page.awt.RecdThread.MP4Threadimport page.awt.RecdThread.GIFThreadimport java.awt.GradientPaintimport utilpc.awt.FG2Dimport page.anim.TreeContimport javax.swing.JTreeimport javax.swing.event.TreeExpansionListenerimport common.util.anim.MaModelimport javax.swing.tree.DefaultMutableTreeNodeimport javax.swing.event.TreeExpansionEventimport java.util.function.IntPredicateimport javax.swing.tree.DefaultTreeModelimport common.util.anim.EAnimDimport page.anim.AnimBoximport utilpc.PPimport common.CommonStatic.BCAuxAssetsimport common.CommonStatic.EditLinkimport page.JBTNimport page.anim.DIYViewPageimport page.anim.ImgCutEditPageimport page.anim.MaModelEditPageimport page.anim.MaAnimEditPageimport page.anim.EditHeadimport java.awt.event.ActionListenerimport page.anim.AbEditPageimport common.util.anim.EAnimSimport page.anim.ModelBoximport common.util.anim.ImgCutimport page.view.AbViewPageimport javax.swing.JListimport javax.swing.JScrollPaneimport javax.swing.JComboBoximport utilpc.UtilPCimport javax.swing.event.ListSelectionListenerimport javax.swing.event.ListSelectionEventimport common.system.VImgimport page.support.AnimLCRimport page.support.AnimTableimport common.util.anim.MaAnimimport java.util.EventObjectimport javax.swing.text.JTextComponentimport page.anim.PartEditTableimport javax.swing.ListSelectionModelimport page.support.AnimTableTHimport page.JTFimport utilpc.ReColorimport page.anim.ImgCutEditTableimport page.anim.SpriteBoximport page.anim.SpriteEditPageimport java.awt.event.FocusAdapterimport java.awt.event.FocusEventimport common.pack.PackData.UserPackimport utilpc.Algorithm.SRResultimport page.anim.MaAnimEditTableimport javax.swing.JSliderimport java.awt.event.MouseWheelEventimport common.util.anim.EPartimport javax.swing.event.ChangeEventimport page.anim.AdvAnimEditPageimport javax.swing.BorderFactoryimport page.JLimport javax.swing.ImageIconimport page.anim.MMTreeimport javax.swing.event.TreeSelectionListenerimport javax.swing.event.TreeSelectionEventimport page.support.AbJTableimport page.anim.MaModelEditTableimport page.info.edit.ProcTableimport page.info.edit.ProcTable.AtkProcTableimport page.info.edit.SwingEditorimport page.info.edit.ProcTable.MainProcTableimport page.support.ListJtfPolicyimport page.info.edit.SwingEditor.SwingEGimport common.util.Data.Procimport java.lang.Runnableimport javax.swing.JComponentimport page.info.edit.LimitTableimport page.pack.CharaGroupPageimport page.pack.LvRestrictPageimport javax.swing.SwingConstantsimport common.util.lang.Editors.EditorGroupimport common.util.lang.Editors.EdiFieldimport common.util.lang.Editorsimport common.util.lang.ProcLangimport page.info.edit.EntityEditPageimport common.util.lang.Editors.EditorSupplierimport common.util.lang.Editors.EditControlimport page.info.edit.SwingEditor.IntEditorimport page.info.edit.SwingEditor.BoolEditorimport page.info.edit.SwingEditor.IdEditorimport page.SupPageimport common.util.unit.AbEnemyimport common.pack.IndexContainer.Indexableimport common.pack.Context.SupExcimport common.battle.data .AtkDataModelimport utilpc.Interpretimport common.battle.data .CustomEntityimport page.info.filter.UnitEditBoximport common.battle.data .CustomUnitimport common.util.stage.SCGroupimport page.info.edit.SCGroupEditTableimport common.util.stage.SCDefimport page.info.filter.EnemyEditBoximport common.battle.data .CustomEnemyimport page.info.StageFilterPageimport page.view.BGViewPageimport page.view.CastleViewPageimport page.view.MusicPageimport common.util.stage.CastleImgimport common.util.stage.CastleListimport java.text.DecimalFormatimport common.util.stage.Recdimport common.util.stage.MapColc.PackMapColcimport page.info.edit.StageEditTableimport page.support.ReorderListimport page.info.edit.HeadEditTableimport page.info.filter.EnemyFindPageimport page.battle.BattleSetupPageimport page.info.edit.AdvStEditPageimport page.battle.StRecdPageimport page.info.edit.LimitEditPageimport page.support.ReorderListenerimport common.util.pack.Soulimport page.info.edit.AtkEditTableimport page.info.filter.UnitFindPageimport common.battle.Basisimport common.util.Data.Proc.IMUimport javax.swing.DefaultComboBoxModelimport common.util.Animableimport common.util.pack.Soul.SoulTypeimport page.view.UnitViewPageimport page.view.EnemyViewPageimport page.info.edit.SwingEditor.EditCtrlimport page.support.Reorderableimport page.info.EnemyInfoPageimport common.util.unit.EneRandimport page.pack.EREditPageimport page.support.InTableTHimport page.support.EnemyTCRimport javax.swing.DefaultListCellRendererimport page.info.filter.UnitListTableimport page.info.filter.UnitFilterBoximport page.info.filter.EnemyListTableimport page.info.filter.EnemyFilterBoximport page.info.filter.UFBButtonimport page.info.filter.UFBListimport common.battle.data .MaskUnitimport javax.swing.AbstractButtonimport page.support.SortTableimport page.info.UnitInfoPageimport page.support.UnitTCRimport page.info.filter.EFBButtonimport page.info.filter.EFBListimport common.util.stage.LvRestrictimport common.util.stage.CharaGroupimport page.info.StageTableimport page.info.TreaTableimport javax.swing.JPanelimport page.info.UnitInfoTableimport page.basis.BasisPageimport kotlin.jvm.JvmOverloadsimport page.info.EnemyInfoTableimport common.util.stage.RandStageimport page.info.StagePageimport page.info.StageRandPageimport common.util.unit.EFormimport page.pack.EREditTableimport common.util.EREntimport common.pack.FixIndexListimport page.support.UnitLCRimport page.pack.RecdPackPageimport page.pack.CastleEditPageimport page.pack.BGEditPageimport page.pack.CGLREditPageimport common.pack.Source.ZipSourceimport page.info.edit.EnemyEditPageimport page.info.edit.StageEditPageimport page.info.StageViewPageimport page.pack.UnitManagePageimport page.pack.MusicEditPageimport page.battle.AbRecdPageimport common.system.files.VFileRootimport java.awt.Desktopimport common.pack.PackDataimport common.util.unit.UnitLevelimport page.info.edit.FormEditPageimport common.util.anim.AnimIimport common.util.anim.AnimI.AnimTypeimport common.util.anim.AnimDimport common.battle.data .Orbimport page.basis.LineUpBoximport page.basis.LubContimport common.battle.BasisLUimport page.basis.ComboListTableimport page.basis.ComboListimport page.basis.NyCasBoximport page.basis.UnitFLUPageimport common.util.unit.Comboimport page.basis.LevelEditPageimport common.util.pack.NyCastleimport common.battle.LineUpimport common.system.SymCoordimport java.util.TreeSetimport page.basis.OrbBoximport javax.swing.table.DefaultTableCellRendererimport javax.swing.JTableimport common.CommonStatic.BattleConstimport common.battle.StageBasisimport common.util.ImgCoreimport common.battle.attack.ContAbimport common.battle.entity.EAnimContimport common.battle.entity.WaprContimport page.battle.RecdManagePageimport page.battle.ComingTableimport common.util.stage.EStageimport page.battle.EntityTableimport common.battle.data .MaskEnemyimport common.battle.SBRplyimport common.battle.entity.AbEntityimport page.battle.RecdSavePageimport page.LocCompimport page.LocSubCompimport javax.swing.table.TableModelimport page.support.TModelimport javax.swing.event.TableModelListenerimport javax.swing.table.DefaultTableColumnModelimport javax.swing.JFileChooserimport javax.swing.filechooser.FileNameExtensionFilterimport javax.swing.TransferHandlerimport java.awt.datatransfer.Transferableimport java.awt.datatransfer.DataFlavorimport javax.swing.DropModeimport javax.swing.TransferHandler.TransferSupportimport java.awt.dnd.DragSourceimport java.awt.datatransfer.UnsupportedFlavorExceptionimport common.system.Copableimport page.support.AnimTransferimport javax.swing.DefaultListModelimport page.support.InListTHimport java.awt.FocusTraversalPolicyimport javax.swing.JTextFieldimport page.CustomCompimport javax.swing.JToggleButtonimport javax.swing.JButtonimport javax.swing.ToolTipManagerimport javax.swing.JRootPaneimport javax.swing.JProgressBarimport page.ConfigPageimport page.view.EffectViewPageimport page.pack.PackEditPageimport page.pack.ResourcePageimport javax.swing.WindowConstantsimport java.awt.event.AWTEventListenerimport java.awt.AWTEventimport java.awt.event.ComponentAdapterimport java.awt.event.ComponentEventimport java.util.ConcurrentModificationExceptionimport javax.swing.plaf.FontUIResourceimport java.util.Enumerationimport javax.swing.UIManagerimport common.CommonStatic.FakeKeyimport page.LocSubComp.LocBinderimport page.LSCPopimport java.awt.BorderLayoutimport java.awt.GridLayoutimport javax.swing.JTextPaneimport page.TTTimport java.util.ResourceBundleimport java.util.MissingResourceExceptionimport java.util.Localeimport common.io.json.Test.JsonTest_2import common.pack.PackData.PackDescimport common.io.PackLoaderimport common.io.PackLoader.Preloadimport common.io.PackLoader.ZipDescimport common.io.json.Testimport common.io.json.JsonClassimport common.io.json.JsonFieldimport common.io.json.JsonField.GenTypeimport common.io.json.Test.JsonTest_0.JsonDimport common.io.json.JsonClass.RTypeimport java.util.HashSetimport common.io.json.JsonDecoder.OnInjectedimport common.io.json.JsonField.IOTypeimport common.io.json.JsonExceptionimport common.io.json.JsonClass.NoTagimport common.io.json.JsonField.SerTypeimport common.io.json.JsonClass.WTypeimport kotlin.reflect.KClassimport com.google.gson.JsonArrayimport common.io.assets.Admin.StaticPermittedimport common.io.json.JsonClass.JCGenericimport common.io.json.JsonClass.JCGetterimport com.google.gson.JsonPrimitiveimport com.google.gson.JsonNullimport common.io.json.JsonClass.JCIdentifierimport java.lang.ClassNotFoundExceptionimport common.io.assets.AssetLoader.AssetHeaderimport common.io.assets.AssetLoader.AssetHeader.AssetEntryimport common.io.InStreamDefimport common.io.BCUExceptionimport java.io.UnsupportedEncodingExceptionimport common.io.OutStreamDefimport javax.crypto.Cipherimport javax.crypto.spec.IvParameterSpecimport javax.crypto.spec.SecretKeySpecimport common.io.PackLoader.FileSaverimport common.system.files.FDByteimport common.io.json.JsonClass.JCConstructorimport common.io.PackLoader.FileLoader.FLStreamimport common.io.PackLoader.PatchFileimport java.lang.NullPointerExceptionimport java.lang.IndexOutOfBoundsExceptionimport common.io.MultiStreamimport java.io.RandomAccessFileimport common.io.MultiStream.TrueStreamimport java.lang.RuntimeExceptionimport common.pack.Source.ResourceLocationimport common.pack.Source.AnimLoaderimport common.pack.Source.SourceAnimLoaderimport common.util.anim.AnimCIimport common.system.files.FDFileimport common.pack.IndexContainerimport common.battle.data .PCoinimport common.util.pack.EffAnimimport common.battle.data .DataEnemyimport common.util.stage.Limit.DefLimitimport common.pack.IndexContainer.Reductorimport common.pack.FixIndexList.FixIndexMapimport common.pack.VerFixer.IdFixerimport common.pack.IndexContainer.IndexContimport common.pack.IndexContainer.ContGetterimport common.util.stage.CastleList.PackCasListimport common.util.Data.Proc.THEMEimport common.CommonStatic.ImgReaderimport common.pack.VerFixerimport common.pack.VerFixer.VerFixerExceptionimport java.lang.NumberFormatExceptionimport common.pack.Source.SourceAnimSaverimport common.pack.VerFixer.EnemyFixerimport common.pack.VerFixer.PackFixerimport common.pack.PackData.DefPackimport java.util.function.BiConsumerimport common.util.BattleStaticimport common.util.anim.AnimU.ImageKeeperimport common.util.anim.AnimCE.AnimCELoaderimport common.util.anim.AnimCI.AnimCIKeeperimport common.util.anim.AnimUD.DefImgLoaderimport common.util.BattleObjimport common.util.Data.Proc.ProcItemimport common.util.lang.ProcLang.ItemLangimport common.util.lang.LocaleCenter.Displayableimport common.util.lang.Editors.DispItemimport common.util.lang.LocaleCenter.ObjBinderimport common.util.lang.LocaleCenter.ObjBinder.BinderFuncimport common.util.Data.Proc.PROBimport org.jcodec.common.tools.MathUtilimport common.util.Data.Proc.PTimport common.util.Data.Proc.PTDimport common.util.Data.Proc.PMimport common.util.Data.Proc.WAVEimport common.util.Data.Proc.WEAKimport common.util.Data.Proc.STRONGimport common.util.Data.Proc.BURROWimport common.util.Data.Proc.REVIVEimport common.util.Data.Proc.SUMMONimport common.util.Data.Proc.MOVEWAVEimport common.util.Data.Proc.POISONimport common.util.Data.Proc.CRITIimport common.util.Data.Proc.VOLCimport common.util.Data.Proc.ARMORimport common.util.Data.Proc.SPEEDimport java.util.LinkedHashMapimport common.util.lang.LocaleCenter.DisplayItemimport common.util.lang.ProcLang.ProcLangStoreimport common.util.lang.Formatter.IntExpimport common.util.lang.Formatter.RefObjimport common.util.lang.Formatter.BoolExpimport common.util.lang.Formatter.BoolElemimport common.util.lang.Formatter.IElemimport common.util.lang.Formatter.Contimport common.util.lang.Formatter.Elemimport common.util.lang.Formatter.RefElemimport common.util.lang.Formatter.RefFieldimport common.util.lang.Formatter.RefFuncimport common.util.lang.Formatter.TextRefimport common.util.lang.Formatter.CodeBlockimport common.util.lang.Formatter.TextPlainimport common.util.unit.Unit.UnitInfoimport common.util.lang.MultiLangCont.MultiLangStaticsimport common.util.pack.EffAnim.EffTypeimport common.util.pack.EffAnim.ArmorEffimport common.util.pack.EffAnim.BarEneEffimport common.util.pack.EffAnim.BarrierEffimport common.util.pack.EffAnim.DefEffimport common.util.pack.EffAnim.WarpEffimport common.util.pack.EffAnim.ZombieEffimport common.util.pack.EffAnim.KBEffimport common.util.pack.EffAnim.SniperEffimport common.util.pack.EffAnim.VolcEffimport common.util.pack.EffAnim.SpeedEffimport common.util.pack.EffAnim.WeakUpEffimport common.util.pack.EffAnim.EffAnimStoreimport common.util.pack.NyCastle.NyTypeimport common.util.pack.WaveAnimimport common.util.pack.WaveAnim.WaveTypeimport common.util.pack.Background.BGWvTypeimport common.util.unit.Form.FormJsonimport common.system.BasedCopableimport common.util.anim.AnimUDimport common.battle.data .DataUnitimport common.battle.entity.EUnitimport common.battle.entity.EEnemyimport common.util.EntRandimport common.util.stage.Recd.Waitimport java.lang.CloneNotSupportedExceptionimport common.util.stage.StageMap.StageMapInfoimport common.util.stage.Stage.StageInfoimport common.util.stage.Limit.PackLimitimport common.util.stage.MapColc.ClipMapColcimport common.util.stage.CastleList.DefCasListimport common.util.stage.MapColc.StItrimport common.util.Data.Proc.IntType.BitCountimport common.util.CopRandimport common.util.LockGLimport java.lang.IllegalAccessExceptionimport common.battle.data .MaskAtkimport common.battle.data .DefaultDataimport common.battle.data .DataAtkimport common.battle.data .MaskEntityimport common.battle.data .DataEntityimport common.battle.attack.AtkModelAbimport common.battle.attack.AttackAbimport common.battle.attack.AttackSimpleimport common.battle.attack.AttackWaveimport common.battle.entity.Cannonimport common.battle.attack.AttackVolcanoimport common.battle.attack.ContWaveAbimport common.battle.attack.ContWaveDefimport common.battle.attack.AtkModelEntityimport common.battle.entity.EntContimport common.battle.attack.ContMoveimport common.battle.attack.ContVolcanoimport common.battle.attack.ContWaveCanonimport common.battle.attack.AtkModelEnemyimport common.battle.attack.AtkModelUnitimport common.battle.attack.AttackCanonimport common.battle.entity.EUnit.OrbHandlerimport common.battle.entity.Entity.AnimManagerimport common.battle.entity.Entity.AtkManagerimport common.battle.entity.Entity.ZombXimport common.battle.entity.Entity.KBManagerimport common.battle.entity.Entity.PoisonTokenimport common.battle.entity.Entity.WeakTokenimport common.battle.Treasureimport common.battle.MirrorSetimport common.battle.Releaseimport common.battle.ELineUpimport common.battle.entity.Sniperimport common.battle.entity.ECastleimport java.util.Dequeimport common.CommonStatic.Itfimport java.lang.Characterimport common.CommonStatic.ImgWriterimport utilpc.awt.FTATimport utilpc.awt.Blenderimport java.awt.RenderingHintsimport utilpc.awt.BIBuilderimport java.awt.CompositeContextimport java.awt.image.Rasterimport java.awt.image.WritableRasterimport utilpc.ColorSetimport utilpc.OggTimeReaderimport utilpc.UtilPC.PCItr.MusicReaderimport utilpc.UtilPC.PCItr.PCALimport javax.swing.UIManager.LookAndFeelInfoimport java.lang.InstantiationExceptionimport javax.swing.UnsupportedLookAndFeelExceptionimport utilpc.Algorithm.ColorShiftimport utilpc.Algorithm.StackRect
object PackLoader {
    private const val HEADER = 16
    private const val PASSWORD = 16
    private const val CHUNK = 1 shl 16
    private const val HEAD_STR = "battlecatsultimate"
    private val HEAD_DATA = getMD5(HEAD_STR.toByteArray(), HEADER)
    private val INIT_VECTOR = getMD5("battlecatsultimate".toByteArray(), 16)
    @Throws(Exception::class)
    fun decrypt(key: ByteArray?): Cipher {
        val iv = IvParameterSpec(INIT_VECTOR)
        val skeySpec = SecretKeySpec(key, "AES")
        val cipher: Cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING")
        cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv)
        return cipher
    }

    @Throws(Exception::class)
    fun encrypt(key: ByteArray?): Cipher {
        val iv = IvParameterSpec(INIT_VECTOR)
        val skeySpec = SecretKeySpec(key, "AES")
        val cipher: Cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING")
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv)
        return cipher
    }

    fun getMD5(data: ByteArray?, len: Int): ByteArray {
        val md5: MessageDigest = Data.Companion.err<MessageDigest>(SupExc<MessageDigest> { MessageDigest.getInstance("MD5") })
        val ans: ByteArray = md5.digest(data)
        return if (ans.size == len) ans else Arrays.copyOf(ans, len)
    }

    @Throws(Exception::class)
    fun read(fis: FileInputStream, asset: AssetHeader) {
        val head = ByteArray(HEADER)
        fis.read(head)
        if (!Arrays.equals(head, HEAD_DATA)) throw Exception("Corrupted File: header not match")
        val len = ByteArray(4)
        fis.read(len)
        val size: Int = DataIO.Companion.toInt(DataIO.Companion.translate(len), 0)
        val data = ByteArray(size)
        fis.read(data)
        val desc = String(data)
        val je: JsonElement = JsonParser.parseString(desc)
        JsonDecoder.Companion.inject<AssetHeader>(je, AssetHeader::class.java, asset)
        asset.offset = HEADER + 4 + size
    }

    @Throws(Exception::class)
    fun readAssets(cont: Preloader, f: File): List<ZipDesc> {
        val fis = FileInputStream(f)
        val header = AssetHeader()
        read(fis, header)
        val ans: MutableList<ZipDesc> = ArrayList()
        var off: Int = header.offset
        for (ent in header.list) {
            val zip = FileLoader(cont, fis, off, f, true).pack
            ans.add(zip)
            off += ent.size
        }
        fis.close()
        return ans
    }

    @Throws(Exception::class)
    fun readPack(cont: Preload?, f: File): ZipDesc {
        val fis = FileInputStream(f)
        val ans = FileLoader(Preloader { desc: ZipDesc? -> cont }, fis, 0, f, false).pack
        fis.close()
        return ans
    }

    @Throws(Exception::class)
    fun write(fos: FileOutputStream, asset: AssetHeader?) {
        fos.write(HEAD_DATA)
        val data: ByteArray = JsonEncoder.Companion.encode(asset).toString().toByteArray()
        val len = ByteArray(4)
        DataIO.Companion.fromInt(len, 0, data.size)
        fos.write(len)
        fos.write(data)
    }

    @Throws(Exception::class)
    fun writePack(dst: File, folder: File, pd: PackDesc, password: String) {
        FileSaver(dst, folder, pd, password)
    }

    private fun regulate(size: Int): Int {
        return if (size and 0xF == 0) size else (size or 0xF) + 1
    }

    interface PatchFile {
        @Throws(Exception::class)
        fun getFile(path: String?): File
    }

    interface Preload {
        fun preload(fd: FileDesc?): Boolean
    }

    interface Preloader {
        fun getPreload(desc: ZipDesc?): Preload
    }

    @JsonClass(read = RType.FILL)
    class ZipDesc {
        @JsonClass
        class FileDesc : FileData {
            @JsonField
            var path: String? = null

            @JsonField
            var size = 0

            @JsonField
            var offset = 0
            var file // writing only
                    : File? = null
            private var pack // reading only
                    : ZipDesc? = null
            val data // preload reading only
                    : FDByte? = null

            constructor(parent: FileSaver, path: String?, f: File) {
                this.path = path
                file = f
                size = f.length().toInt()
                offset = parent.len
                parent.len += if (size and 0xF == 0) size else (size or 0xF) + 1
                parent.len += PASSWORD
            }

            @Deprecated("")
            @JCConstructor
            constructor(desc: ZipDesc?) {
                pack = desc
            }

            override fun getImg(): FakeImage {
                return if (data != null) data.getImg() else Data.Companion.err<FakeImage>(SupExc<FakeImage> { FakeImage.Companion.read(this) })
            }

            override fun getStream(): InputStream {
                return pack!!.readFile(path)
            }

            override fun readLine(): Queue<String?>? {
                return if (data != null) data.readLine() else super@FileData.readLine()
            }

            override fun size(): Int {
                return size
            }
        }

        @JsonField
        var desc: PackDesc? = null

        @JsonField(gen = GenType.GEN)
        var files: Array<FileDesc>
        var tree: VFileRoot<FileDesc> = VFileRoot<FileDesc>(".")
        private var offset = 0
        private var loader: FileLoader? = null

        constructor(pd: PackDesc?, fs: Array<FileDesc>) {
            desc = pd
            files = fs
        }

        private constructor(fl: FileLoader, off: Int) {
            loader = fl
            offset = off
        }

        fun match(data: ByteArray?): Boolean {
            return Arrays.equals(data, loader!!.key)
        }

        @OnInjected
        fun onInjected() {
            for (fd in files) tree.build(fd.path, fd)
        }

        fun readFile(path: String?): InputStream {
            val fd: FileDesc = tree.find(path).getData()
            println("stream requested: " + fd.path + ", " + fd.size)
            return CommonStatic.ctx.noticeErr<FLStream>(SupExc<FLStream> { FLStream(loader, offset + fd.offset, fd.size) },
                    ErrType.ERROR, "failed to read bcuzip at $path")
        }

        @Throws(Exception::class)
        fun unzip(func: PatchFile) {
            val fis: InputStream = FileInputStream(loader!!.file)
            fis.skip(offset.toLong())
            for (fd in files) {
                val n = regulate(fd.size) / PASSWORD
                val dest = func.getFile(fd.path)
                val fos: OutputStream = FileOutputStream(dest)
                val bs = ByteArray(PASSWORD)
                val cipher: Cipher = decrypt(loader!!.key)
                fis.read(bs)
                cipher.update(bs)
                for (i in 0 until n) {
                    fis.read(bs)
                    val ans: ByteArray = if (i == n - 1) cipher.doFinal(bs) else cipher.update(bs)
                    fos.write(ans)
                }
                fos.close()
            }
            fis.close()
        }

        @Throws(Exception::class)
        fun load() {
            for (fd in files) if (loader!!.context.getPreload(this).preload(fd)) fd.data = FDByte(loader!!.decode(fd.size)) else loader!!.fis.skip(regulate(fd.size + PASSWORD).toLong())
        }

        @Throws(Exception::class)
        private fun save(saver: FileSaver) {
            for (fd in files) {
                val fis = FileInputStream(fd.file)
                var rem = fd.size
                var data: ByteArray? = null
                val cipher: Cipher = encrypt(saver.key)
                while (rem > 0) {
                    val size = Math.min(rem, CHUNK)
                    if (data == null || data.size != size) data = ByteArray(size)
                    fis.read(data)
                    rem -= size
                    saver.save(cipher, data, rem == 0)
                }
                fis.close()
            }
        }
    }

    private class FileLoader(val context: Preloader, stream: FileInputStream, off: Int, val file: File, useRAF: Boolean) {
        private class FLStream private constructor(ld: FileLoader, offset: Int, size: Int) : InputStream() {
            private val fis: MultiStream.ByteStream
            private val cipher: Cipher
            private var LEN = 0
            private var len: Int
            private var size: Int
            private val cache: ByteArray
            private var index: Int
            @Throws(IOException::class)
            override fun close() {
                fis.close()
            }

            @Throws(IOException::class)
            override fun read(): Int {
                if (size == 0) return -1
                if (index >= LEN) update()
                return readByte() and 0xff
            }

            @Throws(IOException::class)
            override fun read(b: ByteArray, off: Int, len: Int): Int {
                var off = off
                val rlen = Math.min(len, size)
                if (b == null) {
                    throw NullPointerException()
                } else if (off < 0 || rlen < 0 || rlen > b.size - off) {
                    throw IndexOutOfBoundsException()
                } else if (rlen == 0) {
                    return if (rlen < len) -1 else 0
                }
                var i = rlen
                while (i > 0) {
                    if (index >= LEN) update()
                    val avi = Math.min(i, LEN - index)
                    System.arraycopy(cache, index, b, off, avi)
                    i -= avi
                    index += avi
                    off += avi
                    size -= avi
                }
                return rlen
            }

            private fun readByte(): Byte {
                size--
                return cache[index++]
            }

            @Throws(IOException::class)
            private fun update() {
                val rlen = Math.min(len, LEN)
                fis.read(cache, 0, rlen)
                len -= rlen
                index = 0
                try {
                    if (len == 0) cipher.doFinal(cache, 0, rlen, cache, 0) else cipher.update(cache, 0, rlen, cache, 0)
                } catch (e: Exception) {
                    throw IOException(e)
                }
            }

            init {
                len = if (size and 0xF == 0) size else (size or 0xF) + 1
                for (i in 0..6) {
                    if (i == 6 || len < 1 shl i + 13) {
                        LEN = 1 shl i + 10
                        break
                    }
                }
                cache = ByteArray(LEN)
                this.size = size
                cipher = decrypt(ld.key)
                fis = MultiStream.Companion.getStream(ld.file, offset, ld.useRAF)
                val init = ByteArray(PASSWORD)
                fis.read(init, 0, PASSWORD)
                cipher.update(init, 0, PASSWORD, ByteArray(0), 0)
                index = LEN
            }
        }

        val fis: FileInputStream
        val pack: ZipDesc
        val key: ByteArray
        private val useRAF: Boolean

        @Throws(Exception::class)
        fun decode(size: Int): ByteArray {
            val len = regulate(size) + PASSWORD
            var bs = ByteArray(len)
            fis.read(bs)
            bs = decrypt(key).doFinal(bs)
            return if (bs.size != size) Arrays.copyOf(bs, size) else bs
        }

        init {
            fis = stream
            this.useRAF = useRAF
            val head = ByteArray(HEADER)
            fis.read(head)
            if (!Arrays.equals(head, HEAD_DATA)) throw Exception("Corrupted File: header not match")
            key = ByteArray(PASSWORD)
            fis.read(key)
            val len = ByteArray(4)
            fis.read(len)
            val size: Int = DataIO.Companion.toInt(DataIO.Companion.translate(len), 0)
            val desc = String(decode(size))
            val je: JsonElement = JsonParser.parseString(desc)
            val offset = off + HEADER + PASSWORD * 2 + 4 + regulate(size)
            pack = JsonDecoder.Companion.inject<ZipDesc>(je, ZipDesc::class.java, ZipDesc(this, offset))
            pack.load()
        }
    }

    class FileSaver(dst: File, folder: File, pd: PackDesc, password: String) {
        private val fos: FileOutputStream
        val key: ByteArray
        val len = 0
        private fun addFiles(fs: MutableList<FileDesc>, f: File, path: String) {
            for (str in EXCLUDE) if (f.name == str) return
            if (f.isDirectory) for (fi in f.listFiles()) addFiles(fs, fi, path + f.name + "/") else fs.add(FileDesc(this, path + f.name, f))
        }

        @Throws(Exception::class)
        fun save(cipher: Cipher, bs: ByteArray, end: Boolean) {
            var bs = bs
            if (bs.size and 0xF != 0) bs = Arrays.copyOf(bs, (bs.size or 0xF) + 1)
            bs = if (end) cipher.doFinal(bs) else cipher.update(bs)
            fos.write(bs)
        }

        companion object {
            @StaticPermitted
            private val EXCLUDE = arrayOf(".DS_Store", ".desktop.ini", "__MACOSX")
        }

        init {
            val fs: MutableList<FileDesc> = ArrayList<FileDesc>()
            for (fi in folder.listFiles()) addFiles(fs, fi, "./")
            val desc = ZipDesc(pd, fs.toTypedArray())
            val bytedesc: ByteArray = JsonEncoder.Companion.encode(desc).toString().toByteArray()
            fos = FileOutputStream(dst)
            fos.write(HEAD_DATA)
            key = getMD5(password.toByteArray(), PASSWORD)
            fos.write(key)
            val len = ByteArray(4)
            DataIO.Companion.fromInt(len, 0, bytedesc.size)
            fos.write(len)
            save(encrypt(key), bytedesc, true)
            desc.save(this)
            fos.close()
        }
    }
}