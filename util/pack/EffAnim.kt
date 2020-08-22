package common.util.packimport

import common.pack.Context
import common.util.Data
import java.util.function.Function

com.google.api.client.json.jackson2.JacksonFactoryimport com.google.api.services.drive.DriveScopesimport com.google.api.client.util.store.FileDataStoreFactoryimport com.google.api.client.http.HttpTransportimport com.google.api.services.drive.Driveimport kotlin.Throwsimport java.io.IOExceptionimport io.drive.DriveUtilimport java.io.FileNotFoundExceptionimport java.io.FileInputStreamimport com.google.api.client.googleapis.auth.oauth2.GoogleClientSecretsimport java.io.InputStreamReaderimport com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlowimport com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledAppimport com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiverimport com.google.api.client.googleapis.javanet.GoogleNetHttpTransportimport kotlin.jvm.JvmStaticimport io.drive.DrvieInitimport com.google.api.client.http.javanet.NetHttpTransportimport com.google.api.services.drive.model.FileListimport java.io.BufferedInputStreamimport java.io.FileOutputStreamimport com.google.api.client.googleapis.media.MediaHttpDownloaderimport io.WebFileIOimport io.BCJSONimport page.LoadPageimport org.json.JSONObjectimport org.json.JSONArrayimport main.MainBCUimport main.Optsimport common.CommonStaticimport java.util.TreeMapimport java.util.Arraysimport java.io.BufferedReaderimport io.BCMusicimport common.util.stage.Musicimport io.BCPlayerimport java.util.HashMapimport javax.sound.sampled.Clipimport java.io.ByteArrayInputStreamimport javax.sound.sampled.AudioInputStreamimport javax.sound.sampled.AudioSystemimport javax.sound.sampled.DataLineimport javax.sound.sampled.FloatControlimport javax.sound.sampled.LineEventimport com.google.api.client.googleapis.media.MediaHttpDownloaderProgressListenerimport com.google.api.client.googleapis.media.MediaHttpDownloader.DownloadStateimport common.io.DataIOimport io.BCUReaderimport common.io.InStreamimport com.google.gson.JsonElementimport common.io.json.JsonDecoderimport com.google.gson.JsonObjectimport page.MainFrameimport page.view.ViewBox.Confimport page.MainLocaleimport page.battle.BattleInfoPageimport page.support.Exporterimport page.support.Importerimport common.pack.Context.ErrTypeimport common.util.stage.MapColcimport common.util.stage.MapColc.DefMapColcimport common.util.lang.MultiLangContimport common.util.stage.StageMapimport common.util.unit.Enemyimport io.BCUWriterimport java.text.SimpleDateFormatimport java.io.PrintStreamimport common.io.OutStreamimport common.battle.BasisSetimport res.AnimatedGifEncoderimport java.awt.image.BufferedImageimport javax.imageio.ImageIOimport java.security.MessageDigestimport java.security.NoSuchAlgorithmExceptionimport common.io.json.JsonEncoderimport java.io.FileWriterimport com.google.api.client.http.GenericUrlimport org.apache.http.impl .client.CloseableHttpClientimport org.apache.http.impl .client.HttpClientsimport org.apache.http.client.methods.HttpPostimport org.apache.http.entity.mime.content.FileBodyimport org.apache.http.entity.mime.MultipartEntityBuilderimport org.apache.http.entity.mime.HttpMultipartModeimport org.apache.http.HttpEntityimport org.apache.http.util.EntityUtilsimport com.google.api.client.http.HttpRequestInitializerimport com.google.api.client.http.HttpBackOffUnsuccessfulResponseHandlerimport com.google.api.client.util.ExponentialBackOffimport com.google.api.client.http.HttpBackOffIOExceptionHandlerimport res.NeuQuantimport res.LZWEncoderimport java.io.BufferedOutputStreamimport java.awt.Graphics2Dimport java.awt.image.DataBufferByteimport common.system.fake.FakeImageimport utilpc.awt.FIBIimport jogl.util.AmbImageimport common.system.files.VFileimport jogl.util.GLImageimport com.jogamp.opengl.util.texture.TextureDataimport common.system.Pimport com.jogamp.opengl.util.texture.TextureIOimport jogl.GLStaticimport com.jogamp.opengl.util.texture.awt.AWTTextureIOimport java.awt.AlphaCompositeimport common.system.fake.FakeImage.Markerimport jogl.util.GLGraphicsimport com.jogamp.opengl.GL2import jogl.util.GeoAutoimport com.jogamp.opengl.GL2ES3import com.jogamp.opengl.GLimport common.system.fake.FakeGraphicsimport common.system.fake.FakeTransformimport jogl.util.ResManagerimport jogl.util.GLGraphics.GeomGimport jogl.util.GLGraphics.GLCimport jogl.util.GLGraphics.GLTimport com.jogamp.opengl.GL2ES2import com.jogamp.opengl.util.glsl.ShaderCodeimport com.jogamp.opengl.util.glsl.ShaderProgramimport com.jogamp.opengl.GLExceptionimport jogl.StdGLCimport jogl.Tempimport common.util.anim.AnimUimport common.util.anim.EAnimUimport jogl.util.GLIBimport javax.swing.JFrameimport common.util.anim.AnimCEimport common.util.anim.AnimU.UTypeimport com.jogamp.opengl.util.FPSAnimatorimport com.jogamp.opengl.GLEventListenerimport com.jogamp.opengl.GLAutoDrawableimport page.awt.BBBuilderimport page.battle.BattleBox.OuterBoximport common.battle.SBCtrlimport page.battle.BattleBoximport jogl.GLBattleBoximport common.battle.BattleFieldimport page.anim.IconBoximport jogl.GLIconBoximport jogl.GLBBRecdimport page.awt.RecdThreadimport page.view.ViewBoximport jogl.GLViewBoximport page.view.ViewBox.Controllerimport java.awt.AWTExceptionimport page.battle.BBRecdimport jogl.GLRecorderimport com.jogamp.opengl.GLProfileimport com.jogamp.opengl.GLCapabilitiesimport page.anim.IconBox.IBCtrlimport page.anim.IconBox.IBConfimport page.view.ViewBox.VBExporterimport jogl.GLRecdBImgimport page.JTGimport jogl.GLCstdimport jogl.GLVBExporterimport common.util.anim.EAnimIimport page.RetFuncimport page.battle.BattleBox.BBPainterimport page.battle.BBCtrlimport javax.swing.JOptionPaneimport kotlin.jvm.Strictfpimport main.Invimport javax.swing.SwingUtilitiesimport java.lang.InterruptedExceptionimport utilpc.UtilPC.PCItrimport utilpc.awt.PCIBimport jogl.GLBBBimport page.awt.AWTBBBimport utilpc.Themeimport page.MainPageimport common.io.assets.AssetLoaderimport common.pack.Source.Workspaceimport common.io.PackLoader.ZipDesc.FileDescimport common.io.assets.Adminimport page.awt.BattleBoxDefimport page.awt.IconBoxDefimport page.awt.BBRecdAWTimport page.awt.ViewBoxDefimport org.jcodec.api.awt.AWTSequenceEncoderimport page.awt.RecdThread.PNGThreadimport page.awt.RecdThread.MP4Threadimport page.awt.RecdThread.GIFThreadimport java.awt.GradientPaintimport utilpc.awt.FG2Dimport page.anim.TreeContimport javax.swing.JTreeimport javax.swing.event.TreeExpansionListenerimport common.util.anim.MaModelimport javax.swing.tree.DefaultMutableTreeNodeimport javax.swing.event.TreeExpansionEventimport java.util.function.IntPredicateimport javax.swing.tree.DefaultTreeModelimport common.util.anim.EAnimDimport page.anim.AnimBoximport utilpc.PPimport common.CommonStatic.BCAuxAssetsimport common.CommonStatic.EditLinkimport page.JBTNimport page.anim.DIYViewPageimport page.anim.ImgCutEditPageimport page.anim.MaModelEditPageimport page.anim.MaAnimEditPageimport page.anim.EditHeadimport java.awt.event.ActionListenerimport page.anim.AbEditPageimport common.util.anim.EAnimSimport page.anim.ModelBoximport common.util.anim.ImgCutimport page.view.AbViewPageimport javax.swing.JListimport javax.swing.JScrollPaneimport javax.swing.JComboBoximport utilpc.UtilPCimport javax.swing.event.ListSelectionListenerimport javax.swing.event.ListSelectionEventimport common.system.VImgimport page.support.AnimLCRimport page.support.AnimTableimport common.util.anim.MaAnimimport java.util.EventObjectimport javax.swing.text.JTextComponentimport page.anim.PartEditTableimport javax.swing.ListSelectionModelimport page.support.AnimTableTHimport page.JTFimport utilpc.ReColorimport page.anim.ImgCutEditTableimport page.anim.SpriteBoximport page.anim.SpriteEditPageimport java.awt.event.FocusAdapterimport java.awt.event.FocusEventimport common.pack.PackData.UserPackimport utilpc.Algorithm.SRResultimport page.anim.MaAnimEditTableimport javax.swing.JSliderimport java.awt.event.MouseWheelEventimport common.util.anim.EPartimport javax.swing.event.ChangeEventimport page.anim.AdvAnimEditPageimport javax.swing.BorderFactoryimport page.JLimport javax.swing.ImageIconimport page.anim.MMTreeimport javax.swing.event.TreeSelectionListenerimport javax.swing.event.TreeSelectionEventimport page.support.AbJTableimport page.anim.MaModelEditTableimport page.info.edit.ProcTableimport page.info.edit.ProcTable.AtkProcTableimport page.info.edit.SwingEditorimport page.info.edit.ProcTable.MainProcTableimport page.support.ListJtfPolicyimport page.info.edit.SwingEditor.SwingEGimport common.util.Data.Procimport java.lang.Runnableimport javax.swing.JComponentimport page.info.edit.LimitTableimport page.pack.CharaGroupPageimport page.pack.LvRestrictPageimport javax.swing.SwingConstantsimport common.util.lang.Editors.EditorGroupimport common.util.lang.Editors.EdiFieldimport common.util.lang.Editorsimport common.util.lang.ProcLangimport page.info.edit.EntityEditPageimport common.util.lang.Editors.EditorSupplierimport common.util.lang.Editors.EditControlimport page.info.edit.SwingEditor.IntEditorimport page.info.edit.SwingEditor.BoolEditorimport page.info.edit.SwingEditor.IdEditorimport page.SupPageimport common.util.unit.AbEnemyimport common.pack.IndexContainer.Indexableimport common.pack.Context.SupExcimport common.battle.data .AtkDataModelimport utilpc.Interpretimport common.battle.data .CustomEntityimport page.info.filter.UnitEditBoximport common.battle.data .CustomUnitimport common.util.stage.SCGroupimport page.info.edit.SCGroupEditTableimport common.util.stage.SCDefimport page.info.filter.EnemyEditBoximport common.battle.data .CustomEnemyimport page.info.StageFilterPageimport page.view.BGViewPageimport page.view.CastleViewPageimport page.view.MusicPageimport common.util.stage.CastleImgimport common.util.stage.CastleListimport java.text.DecimalFormatimport common.util.stage.Recdimport common.util.stage.MapColc.PackMapColcimport page.info.edit.StageEditTableimport page.support.ReorderListimport page.info.edit.HeadEditTableimport page.info.filter.EnemyFindPageimport page.battle.BattleSetupPageimport page.info.edit.AdvStEditPageimport page.battle.StRecdPageimport page.info.edit.LimitEditPageimport page.support.ReorderListenerimport common.util.pack.Soulimport page.info.edit.AtkEditTableimport page.info.filter.UnitFindPageimport common.battle.Basisimport common.util.Data.Proc.IMUimport javax.swing.DefaultComboBoxModelimport common.util.Animableimport common.util.pack.Soul.SoulTypeimport page.view.UnitViewPageimport page.view.EnemyViewPageimport page.info.edit.SwingEditor.EditCtrlimport page.support.Reorderableimport page.info.EnemyInfoPageimport common.util.unit.EneRandimport page.pack.EREditPageimport page.support.InTableTHimport page.support.EnemyTCRimport javax.swing.DefaultListCellRendererimport page.info.filter.UnitListTableimport page.info.filter.UnitFilterBoximport page.info.filter.EnemyListTableimport page.info.filter.EnemyFilterBoximport page.info.filter.UFBButtonimport page.info.filter.UFBListimport common.battle.data .MaskUnitimport javax.swing.AbstractButtonimport page.support.SortTableimport page.info.UnitInfoPageimport page.support.UnitTCRimport page.info.filter.EFBButtonimport page.info.filter.EFBListimport common.util.stage.LvRestrictimport common.util.stage.CharaGroupimport page.info.StageTableimport page.info.TreaTableimport javax.swing.JPanelimport page.info.UnitInfoTableimport page.basis.BasisPageimport kotlin.jvm.JvmOverloadsimport page.info.EnemyInfoTableimport common.util.stage.RandStageimport page.info.StagePageimport page.info.StageRandPageimport common.util.unit.EFormimport page.pack.EREditTableimport common.util.EREntimport common.pack.FixIndexListimport page.support.UnitLCRimport page.pack.RecdPackPageimport page.pack.CastleEditPageimport page.pack.BGEditPageimport page.pack.CGLREditPageimport common.pack.Source.ZipSourceimport page.info.edit.EnemyEditPageimport page.info.edit.StageEditPageimport page.info.StageViewPageimport page.pack.UnitManagePageimport page.pack.MusicEditPageimport page.battle.AbRecdPageimport common.system.files.VFileRootimport java.awt.Desktopimport common.pack.PackDataimport common.util.unit.UnitLevelimport page.info.edit.FormEditPageimport common.util.anim.AnimIimport common.util.anim.AnimI.AnimTypeimport common.util.anim.AnimDimport common.battle.data .Orbimport page.basis.LineUpBoximport page.basis.LubContimport common.battle.BasisLUimport page.basis.ComboListTableimport page.basis.ComboListimport page.basis.NyCasBoximport page.basis.UnitFLUPageimport common.util.unit.Comboimport page.basis.LevelEditPageimport common.util.pack.NyCastleimport common.battle.LineUpimport common.system.SymCoordimport java.util.TreeSetimport page.basis.OrbBoximport javax.swing.table.DefaultTableCellRendererimport javax.swing.JTableimport common.CommonStatic.BattleConstimport common.battle.StageBasisimport common.util.ImgCoreimport common.battle.attack.ContAbimport common.battle.entity.EAnimContimport common.battle.entity.WaprContimport page.battle.RecdManagePageimport page.battle.ComingTableimport common.util.stage.EStageimport page.battle.EntityTableimport common.battle.data .MaskEnemyimport common.battle.SBRplyimport common.battle.entity.AbEntityimport page.battle.RecdSavePageimport page.LocCompimport page.LocSubCompimport javax.swing.table.TableModelimport page.support.TModelimport javax.swing.event.TableModelListenerimport javax.swing.table.DefaultTableColumnModelimport javax.swing.JFileChooserimport javax.swing.filechooser.FileNameExtensionFilterimport javax.swing.TransferHandlerimport java.awt.datatransfer.Transferableimport java.awt.datatransfer.DataFlavorimport javax.swing.DropModeimport javax.swing.TransferHandler.TransferSupportimport java.awt.dnd.DragSourceimport java.awt.datatransfer.UnsupportedFlavorExceptionimport common.system.Copableimport page.support.AnimTransferimport javax.swing.DefaultListModelimport page.support.InListTHimport java.awt.FocusTraversalPolicyimport javax.swing.JTextFieldimport page.CustomCompimport javax.swing.JToggleButtonimport javax.swing.JButtonimport javax.swing.ToolTipManagerimport javax.swing.JRootPaneimport javax.swing.JProgressBarimport page.ConfigPageimport page.view.EffectViewPageimport page.pack.PackEditPageimport page.pack.ResourcePageimport javax.swing.WindowConstantsimport java.awt.event.AWTEventListenerimport java.awt.AWTEventimport java.awt.event.ComponentAdapterimport java.awt.event.ComponentEventimport java.util.ConcurrentModificationExceptionimport javax.swing.plaf.FontUIResourceimport java.util.Enumerationimport javax.swing.UIManagerimport common.CommonStatic.FakeKeyimport page.LocSubComp.LocBinderimport page.LSCPopimport java.awt.BorderLayoutimport java.awt.GridLayoutimport javax.swing.JTextPaneimport page.TTTimport java.util.ResourceBundleimport java.util.MissingResourceExceptionimport java.util.Localeimport common.io.json.Test.JsonTest_2import common.pack.PackData.PackDescimport common.io.PackLoaderimport common.io.PackLoader.Preloadimport common.io.PackLoader.ZipDescimport common.io.json.Testimport common.io.json.JsonClassimport common.io.json.JsonFieldimport common.io.json.JsonField.GenTypeimport common.io.json.Test.JsonTest_0.JsonDimport common.io.json.JsonClass.RTypeimport java.util.HashSetimport common.io.json.JsonDecoder.OnInjectedimport common.io.json.JsonField.IOTypeimport common.io.json.JsonExceptionimport common.io.json.JsonClass.NoTagimport common.io.json.JsonField.SerTypeimport common.io.json.JsonClass.WTypeimport kotlin.reflect.KClassimport com.google.gson.JsonArrayimport common.io.assets.Admin.StaticPermittedimport common.io.json.JsonClass.JCGenericimport common.io.json.JsonClass.JCGetterimport com.google.gson.JsonPrimitiveimport com.google.gson.JsonNullimport common.io.json.JsonClass.JCIdentifierimport java.lang.ClassNotFoundExceptionimport common.io.assets.AssetLoader.AssetHeaderimport common.io.assets.AssetLoader.AssetHeader.AssetEntryimport common.io.InStreamDefimport common.io.BCUExceptionimport java.io.UnsupportedEncodingExceptionimport common.io.OutStreamDefimport javax.crypto.Cipherimport javax.crypto.spec.IvParameterSpecimport javax.crypto.spec.SecretKeySpecimport common.io.PackLoader.FileSaverimport common.system.files.FDByteimport common.io.json.JsonClass.JCConstructorimport common.io.PackLoader.FileLoader.FLStreamimport common.io.PackLoader.PatchFileimport java.lang.NullPointerExceptionimport java.lang.IndexOutOfBoundsExceptionimport common.io.MultiStreamimport java.io.RandomAccessFileimport common.io.MultiStream.TrueStreamimport java.lang.RuntimeExceptionimport common.pack.Source.ResourceLocationimport common.pack.Source.AnimLoaderimport common.pack.Source.SourceAnimLoaderimport common.util.anim.AnimCIimport common.system.files.FDFileimport common.pack.IndexContainerimport common.battle.data .PCoinimport common.util.pack.EffAnimimport common.battle.data .DataEnemyimport common.util.stage.Limit.DefLimitimport common.pack.IndexContainer.Reductorimport common.pack.FixIndexList.FixIndexMapimport common.pack.VerFixer.IdFixerimport common.pack.IndexContainer.IndexContimport common.pack.IndexContainer.ContGetterimport common.util.stage.CastleList.PackCasListimport common.util.Data.Proc.THEMEimport common.CommonStatic.ImgReaderimport common.pack.VerFixerimport common.pack.VerFixer.VerFixerExceptionimport java.lang.NumberFormatExceptionimport common.pack.Source.SourceAnimSaverimport common.pack.VerFixer.EnemyFixerimport common.pack.VerFixer.PackFixerimport common.pack.PackData.DefPackimport java.util.function.BiConsumerimport common.util.BattleStaticimport common.util.anim.AnimU.ImageKeeperimport common.util.anim.AnimCE.AnimCELoaderimport common.util.anim.AnimCI.AnimCIKeeperimport common.util.anim.AnimUD.DefImgLoaderimport common.util.BattleObjimport common.util.Data.Proc.ProcItemimport common.util.lang.ProcLang.ItemLangimport common.util.lang.LocaleCenter.Displayableimport common.util.lang.Editors.DispItemimport common.util.lang.LocaleCenter.ObjBinderimport common.util.lang.LocaleCenter.ObjBinder.BinderFuncimport common.util.Data.Proc.PROBimport org.jcodec.common.tools.MathUtilimport common.util.Data.Proc.PTimport common.util.Data.Proc.PTDimport common.util.Data.Proc.PMimport common.util.Data.Proc.WAVEimport common.util.Data.Proc.WEAKimport common.util.Data.Proc.STRONGimport common.util.Data.Proc.BURROWimport common.util.Data.Proc.REVIVEimport common.util.Data.Proc.SUMMONimport common.util.Data.Proc.MOVEWAVEimport common.util.Data.Proc.POISONimport common.util.Data.Proc.CRITIimport common.util.Data.Proc.VOLCimport common.util.Data.Proc.ARMORimport common.util.Data.Proc.SPEEDimport java.util.LinkedHashMapimport common.util.lang.LocaleCenter.DisplayItemimport common.util.lang.ProcLang.ProcLangStoreimport common.util.lang.Formatter.IntExpimport common.util.lang.Formatter.RefObjimport common.util.lang.Formatter.BoolExpimport common.util.lang.Formatter.BoolElemimport common.util.lang.Formatter.IElemimport common.util.lang.Formatter.Contimport common.util.lang.Formatter.Elemimport common.util.lang.Formatter.RefElemimport common.util.lang.Formatter.RefFieldimport common.util.lang.Formatter.RefFuncimport common.util.lang.Formatter.TextRefimport common.util.lang.Formatter.CodeBlockimport common.util.lang.Formatter.TextPlainimport common.util.unit.Unit.UnitInfoimport common.util.lang.MultiLangCont.MultiLangStaticsimport common.util.pack.EffAnim.EffTypeimport common.util.pack.EffAnim.ArmorEffimport common.util.pack.EffAnim.BarEneEffimport common.util.pack.EffAnim.BarrierEffimport common.util.pack.EffAnim.DefEffimport common.util.pack.EffAnim.WarpEffimport common.util.pack.EffAnim.ZombieEffimport common.util.pack.EffAnim.KBEffimport common.util.pack.EffAnim.SniperEffimport common.util.pack.EffAnim.VolcEffimport common.util.pack.EffAnim.SpeedEffimport common.util.pack.EffAnim.WeakUpEffimport common.util.pack.EffAnim.EffAnimStoreimport common.util.pack.NyCastle.NyTypeimport common.util.pack.WaveAnimimport common.util.pack.WaveAnim.WaveTypeimport common.util.pack.Background.BGWvTypeimport common.util.unit.Form.FormJsonimport common.system.BasedCopableimport common.util.anim.AnimUDimport common.battle.data .DataUnitimport common.battle.entity.EUnitimport common.battle.entity.EEnemyimport common.util.EntRandimport common.util.stage.Recd.Waitimport java.lang.CloneNotSupportedExceptionimport common.util.stage.StageMap.StageMapInfoimport common.util.stage.Stage.StageInfoimport common.util.stage.Limit.PackLimitimport common.util.stage.MapColc.ClipMapColcimport common.util.stage.CastleList.DefCasListimport common.util.stage.MapColc.StItrimport common.util.Data.Proc.IntType.BitCountimport common.util.CopRandimport common.util.LockGLimport java.lang.IllegalAccessExceptionimport common.battle.data .MaskAtkimport common.battle.data .DefaultDataimport common.battle.data .DataAtkimport common.battle.data .MaskEntityimport common.battle.data .DataEntityimport common.battle.attack.AtkModelAbimport common.battle.attack.AttackAbimport common.battle.attack.AttackSimpleimport common.battle.attack.AttackWaveimport common.battle.entity.Cannonimport common.battle.attack.AttackVolcanoimport common.battle.attack.ContWaveAbimport common.battle.attack.ContWaveDefimport common.battle.attack.AtkModelEntityimport common.battle.entity.EntContimport common.battle.attack.ContMoveimport common.battle.attack.ContVolcanoimport common.battle.attack.ContWaveCanonimport common.battle.attack.AtkModelEnemyimport common.battle.attack.AtkModelUnitimport common.battle.attack.AttackCanonimport common.battle.entity.EUnit.OrbHandlerimport common.battle.entity.Entity.AnimManagerimport common.battle.entity.Entity.AtkManagerimport common.battle.entity.Entity.ZombXimport common.battle.entity.Entity.KBManagerimport common.battle.entity.Entity.PoisonTokenimport common.battle.entity.Entity.WeakTokenimport common.battle.Treasureimport common.battle.MirrorSetimport common.battle.Releaseimport common.battle.ELineUpimport common.battle.entity.Sniperimport common.battle.entity.ECastleimport java.util.Dequeimport common.CommonStatic.Itfimport java.lang.Characterimport common.CommonStatic.ImgWriterimport utilpc.awt.FTATimport utilpc.awt.Blenderimport java.awt.RenderingHintsimport utilpc.awt.BIBuilderimport java.awt.CompositeContextimport java.awt.image.Rasterimport java.awt.image.WritableRasterimport utilpc.ColorSetimport utilpc.OggTimeReaderimport utilpc.UtilPC.PCItr.MusicReaderimport utilpc.UtilPC.PCItr.PCALimport javax.swing.UIManager.LookAndFeelInfoimport java.lang.InstantiationExceptionimport javax.swing.UnsupportedLookAndFeelExceptionimport utilpc.Algorithm.ColorShiftimport utilpc.Algorithm.StackRect
class EffAnim<T>(st: String?, vi: VImg?, ic: ImgCut, anims: Array<T>) : AnimD<EffAnim<T>?, T>(st) where T : Enum<T>?, T : EffType<T>? {
    enum class ArmorEff(private val path: String) : EffType<ArmorEff?> {
        BUFF("buff"), DEBUFF("debuff");

        override fun path(): String {
            return path
        }
    }

    enum class BarEneEff(private val path: String) : EffType<BarEneEff?> {
        BREAK("_breaker"), DESTR("_destruction");

        override fun path(): String {
            return path
        }
    }

    enum class BarrierEff(private val path: String) : EffType<BarrierEff?> {
        BREAK("_breaker"), DESTR("_destruction"), DURING("_during"), START("_start"), END("_end");

        override fun path(): String {
            return path
        }
    }

    enum class DefEff(private val path: String) : EffType<DefEff?> {
        DEF("");

        override fun path(): String {
            return path
        }
    }

    class EffAnimStore {
        var A_DOWN: EffAnim<DefEff>? = null
        var A_E_DOWN: EffAnim<DefEff>? = null
        var A_UP: EffAnim<DefEff>? = null
        var A_E_UP: EffAnim<DefEff>? = null
        var A_SLOW: EffAnim<DefEff>? = null
        var A_E_SLOW: EffAnim<DefEff>? = null
        var A_STOP: EffAnim<DefEff>? = null
        var A_E_STOP: EffAnim<DefEff>? = null
        var A_SHIELD: EffAnim<DefEff>? = null
        var A_E_SHIELD: EffAnim<DefEff>? = null
        var A_FARATTACK: EffAnim<DefEff>? = null
        var A_E_FARATTACK: EffAnim<DefEff>? = null
        var A_WAVE_INVALID: EffAnim<DefEff>? = null
        var A_E_WAVE_INVALID: EffAnim<DefEff>? = null
        var A_WAVE_STOP: EffAnim<DefEff>? = null
        var A_E_WAVE_STOP: EffAnim<DefEff>? = null
        var A_WAVEGUARD // unused
                : EffAnim<DefEff>? = null
        var A_E_WAVEGUARD // unused
                : EffAnim<DefEff>? = null
        var A_EFF_INV: EffAnim<DefEff>? = null
        var A_EFF_DEF // unused
                : EffAnim<DefEff>? = null
        var A_Z_STRONG: EffAnim<DefEff>? = null
        var A_B: EffAnim<BarrierEff>? = null
        var A_E_B: EffAnim<BarEneEff>? = null
        var A_W: EffAnim<WarpEff>? = null
        var A_W_C: EffAnim<WarpEff>? = null
        var A_CURSE: EffAnim<DefEff>? = null
        var A_ZOMBIE: EffAnim<ZombieEff>? = null
        var A_SHOCKWAVE: EffAnim<DefEff>? = null
        var A_CRIT: EffAnim<DefEff>? = null
        var A_KB: EffAnim<KBEff>? = null
        var A_SNIPER: EffAnim<SniperEff>? = null
        var A_U_ZOMBIE: EffAnim<ZombieEff>? = null
        var A_U_B: EffAnim<BarrierEff>? = null
        var A_U_E_B: EffAnim<BarEneEff>? = null
        var A_SEAL: EffAnim<DefEff>? = null
        var A_POI0: EffAnim<DefEff>? = null
        var A_POI1: EffAnim<DefEff>? = null
        var A_POI2: EffAnim<DefEff>? = null
        var A_POI3: EffAnim<DefEff>? = null
        var A_POI4: EffAnim<DefEff>? = null
        var A_POI5: EffAnim<DefEff>? = null
        var A_POI6: EffAnim<DefEff>? = null
        var A_POI7: EffAnim<DefEff>? = null
        var A_SATK: EffAnim<DefEff>? = null
        var A_IMUATK: EffAnim<DefEff>? = null
        var A_POISON: EffAnim<DefEff>? = null
        var A_VOLC: EffAnim<VolcEff>? = null
        var A_E_VOLC: EffAnim<VolcEff>? = null
        var A_E_CURSE: EffAnim<DefEff>? = null
        var A_WAVE: EffAnim<DefEff>? = null
        var A_E_WAVE: EffAnim<DefEff>? = null
        var A_ARMOR: EffAnim<ArmorEff>? = null
        var A_E_ARMOR: EffAnim<ArmorEff>? = null
        var A_SPEED: EffAnim<SpeedEff>? = null
        var A_E_SPEED: EffAnim<SpeedEff>? = null
        var A_WEAK_UP: EffAnim<WeakUpEff>? = null
        var A_E_WEAK_UP: EffAnim<WeakUpEff>? = null
        fun values(): Array<EffAnim<*>> {
            val fld = EffAnimStore::class.java.declaredFields
            val ans: Array<EffAnim<*>> = arrayOfNulls(fld.size)
            Data.Companion.err(Context.RunExc { for (i in ans.indices) ans[i] = fld[i][this] as EffAnim<*> })
            return ans
        }

        operator fun set(i: Int, eff: EffAnim<DefEff>) {
            Data.Companion.err(Context.RunExc { EffAnimStore::class.java.declaredFields[i][this] = eff })
        }
    }

    interface EffType<T> : AnimType<EffAnim<T>?, T> where T : Enum<T>?, T : EffType<T>? {
        fun path(): String
    }

    enum class KBEff(private val path: String) : EffType<KBEff?> {
        KB("_hb"), SW("_sw"), ASS("_ass");

        override fun path(): String {
            return path
        }
    }

    enum class SniperEff(private val path: String) : EffType<SniperEff?> {
        IDLE("00"), ATK("01");

        override fun path(): String {
            return path
        }
    }

    enum class SpeedEff(private val path: String) : EffType<SpeedEff?> {
        UP("up"), DOWN("down");

        override fun path(): String {
            return path
        }
    }

    enum class VolcEff(private val path: String) : EffType<VolcEff?> {
        START("00"), DURING("01"), END("02");

        override fun path(): String {
            return path
        }
    }

    enum class WarpEff(private val path: String) : EffType<WarpEff?> {
        ENTER("_entrance"), EXIT("_exit");

        override fun path(): String {
            return path
        }
    }

    enum class WeakUpEff(private val path: String) : EffType<WeakUpEff?> {
        UP("up");

        override fun path(): String {
            return path
        }
    }

    enum class ZombieEff(private val path: String) : EffType<ZombieEff?> {
        REVIVE("revive"), DOWN("_down");

        override fun path(): String {
            return path
        }
    }

    private val vimg: VImg?
    private var rev = false
    private var name = ""
    override fun getNum(): FakeImage? {
        return vimg.getImg()
    }

    override fun load() {
        loaded = true
        parts = imgcut.cut(vimg.getImg())
        mamodel = MaModel.Companion.newIns(str + ".mamodel")
        anims = arrayOfNulls<MaAnim>(types.size)
        for (i in types.indices) anims.get(i) = MaAnim.Companion.newIns(str + types.get(i).path() + ".maanim")
        if (rev) revert()
    }

    override fun toString(): String {
        if (name.length > 0) return name
        val ss: Array<String> = str.split("/").toTypedArray()
        return ss[ss.size - 1]
    }

    companion object {
        fun read() {
            val effas: EffAnimStore = CommonStatic.getBCAssets().effas
            val stre = "./org/battle/e1/set_enemy001_zombie"
            val ve = VImg("$stre.png")
            val ice: ImgCut = ImgCut.Companion.newIns("$stre.imgcut")
            val stra = "./org/battle/a/"
            val va = VImg(stra + "000_a.png")
            val ica: ImgCut = ImgCut.Companion.newIns(stra + "000_a.imgcut")
            var ski = "skill00"
            val stfs = arrayOfNulls<String>(4)
            val vfs: Array<VImg?> = arrayOfNulls<VImg>(4)
            val icfs: Array<ImgCut?> = arrayOfNulls<ImgCut>(4)
            for (i in 0..3) {
                stfs[i] = "./org/battle/s$i/"
                vfs[i] = VImg(stfs[i].toString() + ski + i + ".png")
                icfs[i] = ImgCut.Companion.newIns(stfs[i].toString() + ski + i + ".imgcut")
            }
            effas.A_SHOCKWAVE = EffAnim<DefEff>(stra + "boss_welcome", va, ica, common.util.pack.EffAnim.DefEff.values())
            effas.A_CRIT = EffAnim<DefEff>(stra + "critical", va, ica, common.util.pack.EffAnim.DefEff.values())
            effas.A_KB = EffAnim<KBEff>(stra + "kb", va, ica, common.util.pack.EffAnim.KBEff.values())
            effas.A_ZOMBIE = EffAnim<ZombieEff>(stre, ve, ice, common.util.pack.EffAnim.ZombieEff.values())
            effas.A_U_ZOMBIE = EffAnim<ZombieEff>(stre, ve, ice, common.util.pack.EffAnim.ZombieEff.values())
            effas.A_U_ZOMBIE!!.rev = true
            ski = "skill_"
            for (i in Data.Companion.A_PATH.indices) {
                val path = stfs[0] + Data.Companion.A_PATH.get(i) + "/" + ski + Data.Companion.A_PATH.get(i)
                effas[i * 2] = EffAnim<DefEff>(path, vfs[0], icfs[0], common.util.pack.EffAnim.DefEff.values())
                effas[i * 2 + 1] = EffAnim<DefEff>(path + "_e", vfs[0], icfs[0], common.util.pack.EffAnim.DefEff.values())
            }
            effas.A_EFF_INV = EffAnim<DefEff>(stfs[0].toString() + ski + "effect_invalid", vfs[0], icfs[0], common.util.pack.EffAnim.DefEff.values())
            effas.A_EFF_DEF = EffAnim<DefEff>(stfs[0].toString() + ski + "effectdef", vfs[0], icfs[0], common.util.pack.EffAnim.DefEff.values())
            effas.A_Z_STRONG = EffAnim<DefEff>(stfs[1].toString() + ski + "zombie_strong", vfs[1], icfs[1], common.util.pack.EffAnim.DefEff.values())
            effas.A_B = EffAnim<BarrierEff>(stfs[2].toString() + ski + "barrier", vfs[2], icfs[2], common.util.pack.EffAnim.BarrierEff.values())
            effas.A_U_B = EffAnim<BarrierEff>(stfs[2].toString() + ski + "barrier", vfs[2], icfs[2], common.util.pack.EffAnim.BarrierEff.values())
            effas.A_U_B!!.rev = true
            effas.A_E_B = EffAnim<BarEneEff>(stfs[2].toString() + ski + "barrier_e", vfs[2], icfs[2], common.util.pack.EffAnim.BarEneEff.values())
            effas.A_U_E_B = EffAnim<BarEneEff>(stfs[2].toString() + ski + "barrier_e", vfs[2], icfs[2], common.util.pack.EffAnim.BarEneEff.values())
            effas.A_U_E_B!!.rev = true
            effas.A_W = EffAnim<WarpEff>(stfs[2].toString() + ski + "warp", vfs[2], icfs[2], common.util.pack.EffAnim.WarpEff.values())
            effas.A_W_C = EffAnim<WarpEff>(stfs[2].toString() + ski + "warp_chara", vfs[2], icfs[2], common.util.pack.EffAnim.WarpEff.values())
            val strs = "./org/battle/sniper/"
            val strm = "img043"
            val vis = VImg("$strs$strm.png")
            val ics: ImgCut = ImgCut.Companion.newIns("$strs$strm.imgcut")
            effas.A_SNIPER = EffAnim<SniperEff>(strs + "000_snyaipa", vis, ics, common.util.pack.EffAnim.SniperEff.values())
            effas.A_CURSE = EffAnim<DefEff>(stfs[3].toString() + ski + "curse", vfs[3], icfs[3], common.util.pack.EffAnim.DefEff.values())
            readCustom(stfs, icfs)
            val vuw = VImg("./org/battle/s4/skill004.png")
            val icsvuw: ImgCut = ImgCut.Companion.newIns("./org/battle/s4/skill004.imgcut")
            effas.A_WAVE = EffAnim<DefEff>("./org/battle/s4/skill_wave_attack", vuw, icsvuw, common.util.pack.EffAnim.DefEff.values())
            val vew = VImg("./org/battle/s5/skill005.png")
            val icsvew: ImgCut = ImgCut.Companion.newIns("./org/battle/s5/skill005.imgcut")
            effas.A_E_WAVE = EffAnim<DefEff>("./org/battle/s5/skill_wave_attack_e", vew, icsvew, common.util.pack.EffAnim.DefEff.values())
            val vsatk = VImg("./org/battle/s6/skill006.png")
            val icsatk: ImgCut = ImgCut.Companion.newIns("./org/battle/s6/skill006.imgcut")
            effas.A_SATK = EffAnim<DefEff>("./org/battle/s6/strong_attack", vsatk, icsatk, common.util.pack.EffAnim.DefEff.values())
            val viatk = VImg("./org/battle/s7/skill007.png")
            val iciatk: ImgCut = ImgCut.Companion.newIns("./org/battle/s7/skill007.imgcut")
            effas.A_IMUATK = EffAnim<DefEff>("./org/battle/s7/skill_attack_invalid", viatk, iciatk, common.util.pack.EffAnim.DefEff.values())
            val vip = VImg("./org/battle/s8/skill008.png")
            val icp: ImgCut = ImgCut.Companion.newIns("./org/battle/s8/skill008.imgcut")
            effas.A_POISON = EffAnim<DefEff>("./org/battle/s8/skill_percentage_attack", vip, icp, common.util.pack.EffAnim.DefEff.values())
            var vic: VImg? = VImg("./org/battle/s9/skill009.png")
            var icc: ImgCut = ImgCut.Companion.newIns("./org/battle/s9/skill009.imgcut")
            effas.A_VOLC = EffAnim<VolcEff>("./org/battle/s9/skill_volcano", vic, icc, common.util.pack.EffAnim.VolcEff.values())
            vic = VImg("./org/battle/s10/skill010.png")
            icc = ImgCut.Companion.newIns("./org/battle/s10/skill010.imgcut")
            effas.A_E_VOLC = EffAnim<VolcEff>("./org/battle/s10/skill_volcano", vic, icc, common.util.pack.EffAnim.VolcEff.values())
            val vcu = VImg("./org/battle/s11/skill011.png")
            val iccu: ImgCut = ImgCut.Companion.newIns("./org/battle/s11/skill011.imgcut")
            effas.A_E_CURSE = EffAnim<DefEff>("./org/battle/s11/skill_curse_e", vcu, iccu, common.util.pack.EffAnim.DefEff.values())
        }

        private fun excColor(fimg: FakeImage, f: Function<IntArray, Int>) {
            fimg.mark(Marker.RECOLOR)
            val w: Int = fimg.getWidth()
            val h: Int = fimg.getHeight()
            for (i in 0 until w) for (j in 0 until h) {
                var p: Int = fimg.getRGB(i, j)
                val b = p and 255
                val g = p shr 8 and 255
                val r = p shr 16 and 255
                val a = p shr 24
                p = f.apply(intArrayOf(a, r, g, b))
                fimg.setRGB(i, j, p)
            }
            fimg.mark(Marker.RECOLORED)
        }

        private fun readCustom(stfs: Array<String?>, icfs: Array<ImgCut>) {
            val ski = "skill_"
            val effas: EffAnimStore = CommonStatic.getBCAssets().effas
            val vseal = VImg(stfs[3].toString() + "skill003.png")
            excColor(vseal.getImg()) { `is`: IntArray -> `is`[0] shl 24 or (`is`[1] shl 16) or (`is`[3] shl 8) or `is`[2] }
            effas.A_SEAL = EffAnim<DefEff>(stfs[3].toString() + ski + "curse", vseal, icfs[3], common.util.pack.EffAnim.DefEff.values())
            var vpois = VImg(stfs[3].toString() + "skill003.png")
            excColor(vpois.getImg()) { `is`: IntArray -> `is`[0] shl 24 or (`is`[2] shl 16) or (`is`[3] shl 8) or `is`[1] }
            effas.A_POI0 = EffAnim<DefEff>(stfs[3].toString() + ski + "curse", vpois, icfs[3], common.util.pack.EffAnim.DefEff.values())
            effas.A_POI0!!.name = "poison_DF"
            vpois = VImg(stfs[3].toString() + "poison.png")
            effas.A_POI1 = EffAnim<DefEff>(stfs[3].toString() + ski + "curse", vpois, icfs[3], common.util.pack.EffAnim.DefEff.values())
            effas.A_POI1!!.name = "poison_DT0"
            val strpb = stfs[3].toString() + "poisbub/poisbub"
            vpois = VImg("$strpb.png")
            val icpois: ImgCut = ImgCut.Companion.newIns("$strpb.imgcut")
            effas.A_POI2 = EffAnim<DefEff>(strpb, vpois, icpois, common.util.pack.EffAnim.DefEff.values())
            effas.A_POI2!!.name = "poison_purple"
            vpois = VImg("$strpb.png")
            excColor(vpois.getImg()) { `is`: IntArray -> `is`[0] shl 24 or (`is`[1] shl 16) or (`is`[3] shl 8) or `is`[2] }
            effas.A_POI3 = EffAnim<DefEff>(strpb, vpois, icpois, common.util.pack.EffAnim.DefEff.values())
            effas.A_POI3!!.name = "poison_green"
            vpois = VImg("$strpb.png")
            excColor(vpois.getImg()) { `is`: IntArray -> `is`[0] shl 24 or (`is`[2] shl 16) or (`is`[1] shl 8) or `is`[3] }
            effas.A_POI4 = EffAnim<DefEff>(strpb, vpois, icpois, common.util.pack.EffAnim.DefEff.values())
            effas.A_POI4!!.name = "poison_blue"
            vpois = VImg("$strpb.png")
            excColor(vpois.getImg()) { `is`: IntArray -> `is`[0] shl 24 or (`is`[2] shl 16) or (`is`[3] shl 8) or `is`[1] }
            effas.A_POI5 = EffAnim<DefEff>(strpb, vpois, icpois, common.util.pack.EffAnim.DefEff.values())
            effas.A_POI5!!.name = "poison_cyan"
            vpois = VImg("$strpb.png")
            excColor(vpois.getImg()) { `is`: IntArray -> `is`[0] shl 24 or (`is`[3] shl 16) or (`is`[1] shl 8) or `is`[2] }
            effas.A_POI6 = EffAnim<DefEff>(strpb, vpois, icpois, common.util.pack.EffAnim.DefEff.values())
            effas.A_POI6!!.name = "poison_orange"
            vpois = VImg("$strpb.png")
            excColor(vpois.getImg()) { `is`: IntArray -> `is`[0] shl 24 or (`is`[3] shl 16) or (`is`[2] shl 8) or `is`[1] }
            effas.A_POI7 = EffAnim<DefEff>(strpb, vpois, icpois, common.util.pack.EffAnim.DefEff.values())
            effas.A_POI7!!.name = "poison_pink"
            var breaker = stfs[3].toString() + "armor_break/armor_break"
            var vbreak: VImg? = VImg("$breaker.png")
            var icbreak: ImgCut = ImgCut.Companion.newIns("$breaker.imgcut")
            effas.A_ARMOR = EffAnim<ArmorEff>(breaker, vbreak, icbreak, common.util.pack.EffAnim.ArmorEff.values())
            breaker = stfs[3].toString() + "armor_break_e/armor_break_e"
            icbreak = ImgCut.Companion.newIns("$breaker.imgcut")
            vbreak = VImg("$breaker.png")
            effas.A_E_ARMOR = EffAnim<ArmorEff>(breaker, vbreak, icbreak, common.util.pack.EffAnim.ArmorEff.values())
            var speed = stfs[3].toString() + "speed/speed"
            var vspeed: VImg? = VImg("$speed.png")
            var icspeed: ImgCut = ImgCut.Companion.newIns("$speed.imgcut")
            effas.A_SPEED = EffAnim<SpeedEff>(speed, vspeed, icspeed, common.util.pack.EffAnim.SpeedEff.values())
            speed = stfs[3].toString() + "speed_e/speed_e"
            vspeed = VImg("$speed.png")
            icspeed = ImgCut.Companion.newIns("$speed.imgcut")
            effas.A_E_SPEED = EffAnim<SpeedEff>(speed, vspeed, icspeed, common.util.pack.EffAnim.SpeedEff.values())
            val wea = "./org/battle/"
            var weakup = wea + "weaken_up/weaken_up"
            var vwea: VImg? = VImg("$weakup.png")
            var icwea: ImgCut = ImgCut.Companion.newIns("$weakup.imgcut")
            effas.A_WEAK_UP = EffAnim<WeakUpEff>(weakup, vwea, icwea, common.util.pack.EffAnim.WeakUpEff.values())
            weakup = wea + "weaken_up_e/weaken_up_e"
            vwea = VImg("$weakup.png")
            icwea = ImgCut.Companion.newIns("$weakup.imgcut")
            effas.A_E_WEAK_UP = EffAnim<WeakUpEff>(weakup, vwea, icwea, common.util.pack.EffAnim.WeakUpEff.values())
        }
    }

    init {
        vimg = vi
        imgcut = ic
        types = anims
    }
}