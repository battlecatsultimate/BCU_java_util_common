package common.packimport

import common.pack.Source
import common.pack.UserProfile
import common.util.Data
import common.util.Res
import common.util.pack.Background
import common.util.unit.Unit
import java.io.File
import java.lang.reflect.Method
import java.util.*
import java.util.function.Consumer
import java.util.function.Supplier

com.google.api.client.json.jackson2.JacksonFactoryimport com.google.api.services.drive.DriveScopesimport com.google.api.client.util.store.FileDataStoreFactoryimport com.google.api.client.http.HttpTransportimport com.google.api.services.drive.Driveimport kotlin.Throwsimport java.io.IOExceptionimport io.drive.DriveUtilimport java.io.FileNotFoundExceptionimport java.io.FileInputStreamimport com.google.api.client.googleapis.auth.oauth2.GoogleClientSecretsimport java.io.InputStreamReaderimport com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlowimport com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledAppimport com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiverimport com.google.api.client.googleapis.javanet.GoogleNetHttpTransportimport kotlin.jvm.JvmStaticimport io.drive.DrvieInitimport com.google.api.client.http.javanet.NetHttpTransportimport com.google.api.services.drive.model.FileListimport java.io.BufferedInputStreamimport java.io.FileOutputStreamimport com.google.api.client.googleapis.media.MediaHttpDownloaderimport io.WebFileIOimport io.BCJSONimport page.LoadPageimport org.json.JSONObjectimport org.json.JSONArrayimport main.MainBCUimport main.Optsimport common.CommonStaticimport java.util.TreeMapimport java.util.Arraysimport java.io.BufferedReaderimport io.BCMusicimport common.util.stage.Musicimport io.BCPlayerimport java.util.HashMapimport javax.sound.sampled.Clipimport java.io.ByteArrayInputStreamimport javax.sound.sampled.AudioInputStreamimport javax.sound.sampled.AudioSystemimport javax.sound.sampled.DataLineimport javax.sound.sampled.FloatControlimport javax.sound.sampled.LineEventimport com.google.api.client.googleapis.media.MediaHttpDownloaderProgressListenerimport com.google.api.client.googleapis.media.MediaHttpDownloader.DownloadStateimport common.io.DataIOimport io.BCUReaderimport common.io.InStreamimport com.google.gson.JsonElementimport common.io.json.JsonDecoderimport com.google.gson.JsonObjectimport page.MainFrameimport page.view.ViewBox.Confimport page.MainLocaleimport page.battle.BattleInfoPageimport page.support.Exporterimport page.support.Importerimport common.pack.Context.ErrTypeimport common.util.stage.MapColcimport common.util.stage.MapColc.DefMapColcimport common.util.lang.MultiLangContimport common.util.stage.StageMapimport common.util.unit.Enemyimport io.BCUWriterimport java.text.SimpleDateFormatimport java.io.PrintStreamimport common.io.OutStreamimport common.battle.BasisSetimport res.AnimatedGifEncoderimport java.awt.image.BufferedImageimport javax.imageio.ImageIOimport java.security.MessageDigestimport java.security.NoSuchAlgorithmExceptionimport common.io.json.JsonEncoderimport java.io.FileWriterimport com.google.api.client.http.GenericUrlimport org.apache.http.impl .client.CloseableHttpClientimport org.apache.http.impl .client.HttpClientsimport org.apache.http.client.methods.HttpPostimport org.apache.http.entity.mime.content.FileBodyimport org.apache.http.entity.mime.MultipartEntityBuilderimport org.apache.http.entity.mime.HttpMultipartModeimport org.apache.http.HttpEntityimport org.apache.http.util.EntityUtilsimport com.google.api.client.http.HttpRequestInitializerimport com.google.api.client.http.HttpBackOffUnsuccessfulResponseHandlerimport com.google.api.client.util.ExponentialBackOffimport com.google.api.client.http.HttpBackOffIOExceptionHandlerimport res.NeuQuantimport res.LZWEncoderimport java.io.BufferedOutputStreamimport java.awt.Graphics2Dimport java.awt.image.DataBufferByteimport common.system.fake.FakeImageimport utilpc.awt.FIBIimport jogl.util.AmbImageimport common.system.files.VFileimport jogl.util.GLImageimport com.jogamp.opengl.util.texture.TextureDataimport common.system.Pimport com.jogamp.opengl.util.texture.TextureIOimport jogl.GLStaticimport com.jogamp.opengl.util.texture.awt.AWTTextureIOimport java.awt.AlphaCompositeimport common.system.fake.FakeImage.Markerimport jogl.util.GLGraphicsimport com.jogamp.opengl.GL2import jogl.util.GeoAutoimport com.jogamp.opengl.GL2ES3import com.jogamp.opengl.GLimport common.system.fake.FakeGraphicsimport common.system.fake.FakeTransformimport jogl.util.ResManagerimport jogl.util.GLGraphics.GeomGimport jogl.util.GLGraphics.GLCimport jogl.util.GLGraphics.GLTimport com.jogamp.opengl.GL2ES2import com.jogamp.opengl.util.glsl.ShaderCodeimport com.jogamp.opengl.util.glsl.ShaderProgramimport com.jogamp.opengl.GLExceptionimport jogl.StdGLCimport jogl.Tempimport common.util.anim.AnimUimport common.util.anim.EAnimUimport jogl.util.GLIBimport javax.swing.JFrameimport common.util.anim.AnimCEimport common.util.anim.AnimU.UTypeimport com.jogamp.opengl.util.FPSAnimatorimport com.jogamp.opengl.GLEventListenerimport com.jogamp.opengl.GLAutoDrawableimport page.awt.BBBuilderimport page.battle.BattleBox.OuterBoximport common.battle.SBCtrlimport page.battle.BattleBoximport jogl.GLBattleBoximport common.battle.BattleFieldimport page.anim.IconBoximport jogl.GLIconBoximport jogl.GLBBRecdimport page.awt.RecdThreadimport page.view.ViewBoximport jogl.GLViewBoximport page.view.ViewBox.Controllerimport java.awt.AWTExceptionimport page.battle.BBRecdimport jogl.GLRecorderimport com.jogamp.opengl.GLProfileimport com.jogamp.opengl.GLCapabilitiesimport page.anim.IconBox.IBCtrlimport page.anim.IconBox.IBConfimport page.view.ViewBox.VBExporterimport jogl.GLRecdBImgimport page.JTGimport jogl.GLCstdimport jogl.GLVBExporterimport common.util.anim.EAnimIimport page.RetFuncimport page.battle.BattleBox.BBPainterimport page.battle.BBCtrlimport javax.swing.JOptionPaneimport kotlin.jvm.Strictfpimport main.Invimport javax.swing.SwingUtilitiesimport java.lang.InterruptedExceptionimport utilpc.UtilPC.PCItrimport utilpc.awt.PCIBimport jogl.GLBBBimport page.awt.AWTBBBimport utilpc.Themeimport page.MainPageimport common.io.assets.AssetLoaderimport common.pack.Source.Workspaceimport common.io.PackLoader.ZipDesc.FileDescimport common.io.assets.Adminimport page.awt.BattleBoxDefimport page.awt.IconBoxDefimport page.awt.BBRecdAWTimport page.awt.ViewBoxDefimport org.jcodec.api.awt.AWTSequenceEncoderimport page.awt.RecdThread.PNGThreadimport page.awt.RecdThread.MP4Threadimport page.awt.RecdThread.GIFThreadimport java.awt.GradientPaintimport utilpc.awt.FG2Dimport page.anim.TreeContimport javax.swing.JTreeimport javax.swing.event.TreeExpansionListenerimport common.util.anim.MaModelimport javax.swing.tree.DefaultMutableTreeNodeimport javax.swing.event.TreeExpansionEventimport java.util.function.IntPredicateimport javax.swing.tree.DefaultTreeModelimport common.util.anim.EAnimDimport page.anim.AnimBoximport utilpc.PPimport common.CommonStatic.BCAuxAssetsimport common.CommonStatic.EditLinkimport page.JBTNimport page.anim.DIYViewPageimport page.anim.ImgCutEditPageimport page.anim.MaModelEditPageimport page.anim.MaAnimEditPageimport page.anim.EditHeadimport java.awt.event.ActionListenerimport page.anim.AbEditPageimport common.util.anim.EAnimSimport page.anim.ModelBoximport common.util.anim.ImgCutimport page.view.AbViewPageimport javax.swing.JListimport javax.swing.JScrollPaneimport javax.swing.JComboBoximport utilpc.UtilPCimport javax.swing.event.ListSelectionListenerimport javax.swing.event.ListSelectionEventimport common.system.VImgimport page.support.AnimLCRimport page.support.AnimTableimport common.util.anim.MaAnimimport java.util.EventObjectimport javax.swing.text.JTextComponentimport page.anim.PartEditTableimport javax.swing.ListSelectionModelimport page.support.AnimTableTHimport page.JTFimport utilpc.ReColorimport page.anim.ImgCutEditTableimport page.anim.SpriteBoximport page.anim.SpriteEditPageimport java.awt.event.FocusAdapterimport java.awt.event.FocusEventimport common.pack.PackData.UserPackimport utilpc.Algorithm.SRResultimport page.anim.MaAnimEditTableimport javax.swing.JSliderimport java.awt.event.MouseWheelEventimport common.util.anim.EPartimport javax.swing.event.ChangeEventimport page.anim.AdvAnimEditPageimport javax.swing.BorderFactoryimport page.JLimport javax.swing.ImageIconimport page.anim.MMTreeimport javax.swing.event.TreeSelectionListenerimport javax.swing.event.TreeSelectionEventimport page.support.AbJTableimport page.anim.MaModelEditTableimport page.info.edit.ProcTableimport page.info.edit.ProcTable.AtkProcTableimport page.info.edit.SwingEditorimport page.info.edit.ProcTable.MainProcTableimport page.support.ListJtfPolicyimport page.info.edit.SwingEditor.SwingEGimport common.util.Data.Procimport java.lang.Runnableimport javax.swing.JComponentimport page.info.edit.LimitTableimport page.pack.CharaGroupPageimport page.pack.LvRestrictPageimport javax.swing.SwingConstantsimport common.util.lang.Editors.EditorGroupimport common.util.lang.Editors.EdiFieldimport common.util.lang.Editorsimport common.util.lang.ProcLangimport page.info.edit.EntityEditPageimport common.util.lang.Editors.EditorSupplierimport common.util.lang.Editors.EditControlimport page.info.edit.SwingEditor.IntEditorimport page.info.edit.SwingEditor.BoolEditorimport page.info.edit.SwingEditor.IdEditorimport page.SupPageimport common.util.unit.AbEnemyimport common.pack.IndexContainer.Indexableimport common.pack.Context.SupExcimport common.battle.data .AtkDataModelimport utilpc.Interpretimport common.battle.data .CustomEntityimport page.info.filter.UnitEditBoximport common.battle.data .CustomUnitimport common.util.stage.SCGroupimport page.info.edit.SCGroupEditTableimport common.util.stage.SCDefimport page.info.filter.EnemyEditBoximport common.battle.data .CustomEnemyimport page.info.StageFilterPageimport page.view.BGViewPageimport page.view.CastleViewPageimport page.view.MusicPageimport common.util.stage.CastleImgimport common.util.stage.CastleListimport java.text.DecimalFormatimport common.util.stage.Recdimport common.util.stage.MapColc.PackMapColcimport page.info.edit.StageEditTableimport page.support.ReorderListimport page.info.edit.HeadEditTableimport page.info.filter.EnemyFindPageimport page.battle.BattleSetupPageimport page.info.edit.AdvStEditPageimport page.battle.StRecdPageimport page.info.edit.LimitEditPageimport page.support.ReorderListenerimport common.util.pack.Soulimport page.info.edit.AtkEditTableimport page.info.filter.UnitFindPageimport common.battle.Basisimport common.util.Data.Proc.IMUimport javax.swing.DefaultComboBoxModelimport common.util.Animableimport common.util.pack.Soul.SoulTypeimport page.view.UnitViewPageimport page.view.EnemyViewPageimport page.info.edit.SwingEditor.EditCtrlimport page.support.Reorderableimport page.info.EnemyInfoPageimport common.util.unit.EneRandimport page.pack.EREditPageimport page.support.InTableTHimport page.support.EnemyTCRimport javax.swing.DefaultListCellRendererimport page.info.filter.UnitListTableimport page.info.filter.UnitFilterBoximport page.info.filter.EnemyListTableimport page.info.filter.EnemyFilterBoximport page.info.filter.UFBButtonimport page.info.filter.UFBListimport common.battle.data .MaskUnitimport javax.swing.AbstractButtonimport page.support.SortTableimport page.info.UnitInfoPageimport page.support.UnitTCRimport page.info.filter.EFBButtonimport page.info.filter.EFBListimport common.util.stage.LvRestrictimport common.util.stage.CharaGroupimport page.info.StageTableimport page.info.TreaTableimport javax.swing.JPanelimport page.info.UnitInfoTableimport page.basis.BasisPageimport kotlin.jvm.JvmOverloadsimport page.info.EnemyInfoTableimport common.util.stage.RandStageimport page.info.StagePageimport page.info.StageRandPageimport common.util.unit.EFormimport page.pack.EREditTableimport common.util.EREntimport common.pack.FixIndexListimport page.support.UnitLCRimport page.pack.RecdPackPageimport page.pack.CastleEditPageimport page.pack.BGEditPageimport page.pack.CGLREditPageimport common.pack.Source.ZipSourceimport page.info.edit.EnemyEditPageimport page.info.edit.StageEditPageimport page.info.StageViewPageimport page.pack.UnitManagePageimport page.pack.MusicEditPageimport page.battle.AbRecdPageimport common.system.files.VFileRootimport java.awt.Desktopimport common.pack.PackDataimport common.util.unit.UnitLevelimport page.info.edit.FormEditPageimport common.util.anim.AnimIimport common.util.anim.AnimI.AnimTypeimport common.util.anim.AnimDimport common.battle.data .Orbimport page.basis.LineUpBoximport page.basis.LubContimport common.battle.BasisLUimport page.basis.ComboListTableimport page.basis.ComboListimport page.basis.NyCasBoximport page.basis.UnitFLUPageimport common.util.unit.Comboimport page.basis.LevelEditPageimport common.util.pack.NyCastleimport common.battle.LineUpimport common.system.SymCoordimport java.util.TreeSetimport page.basis.OrbBoximport javax.swing.table.DefaultTableCellRendererimport javax.swing.JTableimport common.CommonStatic.BattleConstimport common.battle.StageBasisimport common.util.ImgCoreimport common.battle.attack.ContAbimport common.battle.entity.EAnimContimport common.battle.entity.WaprContimport page.battle.RecdManagePageimport page.battle.ComingTableimport common.util.stage.EStageimport page.battle.EntityTableimport common.battle.data .MaskEnemyimport common.battle.SBRplyimport common.battle.entity.AbEntityimport page.battle.RecdSavePageimport page.LocCompimport page.LocSubCompimport javax.swing.table.TableModelimport page.support.TModelimport javax.swing.event.TableModelListenerimport javax.swing.table.DefaultTableColumnModelimport javax.swing.JFileChooserimport javax.swing.filechooser.FileNameExtensionFilterimport javax.swing.TransferHandlerimport java.awt.datatransfer.Transferableimport java.awt.datatransfer.DataFlavorimport javax.swing.DropModeimport javax.swing.TransferHandler.TransferSupportimport java.awt.dnd.DragSourceimport java.awt.datatransfer.UnsupportedFlavorExceptionimport common.system.Copableimport page.support.AnimTransferimport javax.swing.DefaultListModelimport page.support.InListTHimport java.awt.FocusTraversalPolicyimport javax.swing.JTextFieldimport page.CustomCompimport javax.swing.JToggleButtonimport javax.swing.JButtonimport javax.swing.ToolTipManagerimport javax.swing.JRootPaneimport javax.swing.JProgressBarimport page.ConfigPageimport page.view.EffectViewPageimport page.pack.PackEditPageimport page.pack.ResourcePageimport javax.swing.WindowConstantsimport java.awt.event.AWTEventListenerimport java.awt.AWTEventimport java.awt.event.ComponentAdapterimport java.awt.event.ComponentEventimport java.util.ConcurrentModificationExceptionimport javax.swing.plaf.FontUIResourceimport java.util.Enumerationimport javax.swing.UIManagerimport common.CommonStatic.FakeKeyimport page.LocSubComp.LocBinderimport page.LSCPopimport java.awt.BorderLayoutimport java.awt.GridLayoutimport javax.swing.JTextPaneimport page.TTTimport java.util.ResourceBundleimport java.util.MissingResourceExceptionimport java.util.Localeimport common.io.json.Test.JsonTest_2import common.pack.PackData.PackDescimport common.io.PackLoaderimport common.io.PackLoader.Preloadimport common.io.PackLoader.ZipDescimport common.io.json.Testimport common.io.json.JsonClassimport common.io.json.JsonFieldimport common.io.json.JsonField.GenTypeimport common.io.json.Test.JsonTest_0.JsonDimport common.io.json.JsonClass.RTypeimport java.util.HashSetimport common.io.json.JsonDecoder.OnInjectedimport common.io.json.JsonField.IOTypeimport common.io.json.JsonExceptionimport common.io.json.JsonClass.NoTagimport common.io.json.JsonField.SerTypeimport common.io.json.JsonClass.WTypeimport kotlin.reflect.KClassimport com.google.gson.JsonArrayimport common.io.assets.Admin.StaticPermittedimport common.io.json.JsonClass.JCGenericimport common.io.json.JsonClass.JCGetterimport com.google.gson.JsonPrimitiveimport com.google.gson.JsonNullimport common.io.json.JsonClass.JCIdentifierimport java.lang.ClassNotFoundExceptionimport common.io.assets.AssetLoader.AssetHeaderimport common.io.assets.AssetLoader.AssetHeader.AssetEntryimport common.io.InStreamDefimport common.io.BCUExceptionimport java.io.UnsupportedEncodingExceptionimport common.io.OutStreamDefimport javax.crypto.Cipherimport javax.crypto.spec.IvParameterSpecimport javax.crypto.spec.SecretKeySpecimport common.io.PackLoader.FileSaverimport common.system.files.FDByteimport common.io.json.JsonClass.JCConstructorimport common.io.PackLoader.FileLoader.FLStreamimport common.io.PackLoader.PatchFileimport java.lang.NullPointerExceptionimport java.lang.IndexOutOfBoundsExceptionimport common.io.MultiStreamimport java.io.RandomAccessFileimport common.io.MultiStream.TrueStreamimport java.lang.RuntimeExceptionimport common.pack.Source.ResourceLocationimport common.pack.Source.AnimLoaderimport common.pack.Source.SourceAnimLoaderimport common.util.anim.AnimCIimport common.system.files.FDFileimport common.pack.IndexContainerimport common.battle.data .PCoinimport common.util.pack.EffAnimimport common.battle.data .DataEnemyimport common.util.stage.Limit.DefLimitimport common.pack.IndexContainer.Reductorimport common.pack.FixIndexList.FixIndexMapimport common.pack.VerFixer.IdFixerimport common.pack.IndexContainer.IndexContimport common.pack.IndexContainer.ContGetterimport common.util.stage.CastleList.PackCasListimport common.util.Data.Proc.THEMEimport common.CommonStatic.ImgReaderimport common.pack.VerFixerimport common.pack.VerFixer.VerFixerExceptionimport java.lang.NumberFormatExceptionimport common.pack.Source.SourceAnimSaverimport common.pack.VerFixer.EnemyFixerimport common.pack.VerFixer.PackFixerimport common.pack.PackData.DefPackimport java.util.function.BiConsumerimport common.util.BattleStaticimport common.util.anim.AnimU.ImageKeeperimport common.util.anim.AnimCE.AnimCELoaderimport common.util.anim.AnimCI.AnimCIKeeperimport common.util.anim.AnimUD.DefImgLoaderimport common.util.BattleObjimport common.util.Data.Proc.ProcItemimport common.util.lang.ProcLang.ItemLangimport common.util.lang.LocaleCenter.Displayableimport common.util.lang.Editors.DispItemimport common.util.lang.LocaleCenter.ObjBinderimport common.util.lang.LocaleCenter.ObjBinder.BinderFuncimport common.util.Data.Proc.PROBimport org.jcodec.common.tools.MathUtilimport common.util.Data.Proc.PTimport common.util.Data.Proc.PTDimport common.util.Data.Proc.PMimport common.util.Data.Proc.WAVEimport common.util.Data.Proc.WEAKimport common.util.Data.Proc.STRONGimport common.util.Data.Proc.BURROWimport common.util.Data.Proc.REVIVEimport common.util.Data.Proc.SUMMONimport common.util.Data.Proc.MOVEWAVEimport common.util.Data.Proc.POISONimport common.util.Data.Proc.CRITIimport common.util.Data.Proc.VOLCimport common.util.Data.Proc.ARMORimport common.util.Data.Proc.SPEEDimport java.util.LinkedHashMapimport common.util.lang.LocaleCenter.DisplayItemimport common.util.lang.ProcLang.ProcLangStoreimport common.util.lang.Formatter.IntExpimport common.util.lang.Formatter.RefObjimport common.util.lang.Formatter.BoolExpimport common.util.lang.Formatter.BoolElemimport common.util.lang.Formatter.IElemimport common.util.lang.Formatter.Contimport common.util.lang.Formatter.Elemimport common.util.lang.Formatter.RefElemimport common.util.lang.Formatter.RefFieldimport common.util.lang.Formatter.RefFuncimport common.util.lang.Formatter.TextRefimport common.util.lang.Formatter.CodeBlockimport common.util.lang.Formatter.TextPlainimport common.util.unit.Unit.UnitInfoimport common.util.lang.MultiLangCont.MultiLangStaticsimport common.util.pack.EffAnim.EffTypeimport common.util.pack.EffAnim.ArmorEffimport common.util.pack.EffAnim.BarEneEffimport common.util.pack.EffAnim.BarrierEffimport common.util.pack.EffAnim.DefEffimport common.util.pack.EffAnim.WarpEffimport common.util.pack.EffAnim.ZombieEffimport common.util.pack.EffAnim.KBEffimport common.util.pack.EffAnim.SniperEffimport common.util.pack.EffAnim.VolcEffimport common.util.pack.EffAnim.SpeedEffimport common.util.pack.EffAnim.WeakUpEffimport common.util.pack.EffAnim.EffAnimStoreimport common.util.pack.NyCastle.NyTypeimport common.util.pack.WaveAnimimport common.util.pack.WaveAnim.WaveTypeimport common.util.pack.Background.BGWvTypeimport common.util.unit.Form.FormJsonimport common.system.BasedCopableimport common.util.anim.AnimUDimport common.battle.data .DataUnitimport common.battle.entity.EUnitimport common.battle.entity.EEnemyimport common.util.EntRandimport common.util.stage.Recd.Waitimport java.lang.CloneNotSupportedExceptionimport common.util.stage.StageMap.StageMapInfoimport common.util.stage.Stage.StageInfoimport common.util.stage.Limit.PackLimitimport common.util.stage.MapColc.ClipMapColcimport common.util.stage.CastleList.DefCasListimport common.util.stage.MapColc.StItrimport common.util.Data.Proc.IntType.BitCountimport common.util.CopRandimport common.util.LockGLimport java.lang.IllegalAccessExceptionimport common.battle.data .MaskAtkimport common.battle.data .DefaultDataimport common.battle.data .DataAtkimport common.battle.data .MaskEntityimport common.battle.data .DataEntityimport common.battle.attack.AtkModelAbimport common.battle.attack.AttackAbimport common.battle.attack.AttackSimpleimport common.battle.attack.AttackWaveimport common.battle.entity.Cannonimport common.battle.attack.AttackVolcanoimport common.battle.attack.ContWaveAbimport common.battle.attack.ContWaveDefimport common.battle.attack.AtkModelEntityimport common.battle.entity.EntContimport common.battle.attack.ContMoveimport common.battle.attack.ContVolcanoimport common.battle.attack.ContWaveCanonimport common.battle.attack.AtkModelEnemyimport common.battle.attack.AtkModelUnitimport common.battle.attack.AttackCanonimport common.battle.entity.EUnit.OrbHandlerimport common.battle.entity.Entity.AnimManagerimport common.battle.entity.Entity.AtkManagerimport common.battle.entity.Entity.ZombXimport common.battle.entity.Entity.KBManagerimport common.battle.entity.Entity.PoisonTokenimport common.battle.entity.Entity.WeakTokenimport common.battle.Treasureimport common.battle.MirrorSetimport common.battle.Releaseimport common.battle.ELineUpimport common.battle.entity.Sniperimport common.battle.entity.ECastleimport java.util.Dequeimport common.CommonStatic.Itfimport java.lang.Characterimport common.CommonStatic.ImgWriterimport utilpc.awt.FTATimport utilpc.awt.Blenderimport java.awt.RenderingHintsimport utilpc.awt.BIBuilderimport java.awt.CompositeContextimport java.awt.image.Rasterimport java.awt.image.WritableRasterimport utilpc.ColorSetimport utilpc.OggTimeReaderimport utilpc.UtilPC.PCItr.MusicReaderimport utilpc.UtilPC.PCItr.PCALimport javax.swing.UIManager.LookAndFeelInfoimport java.lang.InstantiationExceptionimport javax.swing.UnsupportedLookAndFeelExceptionimport utilpc.Algorithm.ColorShiftimport utilpc.Algorithm.StackRect
@JsonClass(read = RType.FILL, noTag = NoTag.LOAD)
abstract class PackData : IndexContainer {
    class DefPack : PackData() {
        var root: VFileRoot<FileDesc> = VFileRoot<FileDesc>(".")
        override fun getID(): String? {
            return common.pack.PackData.Identifier.Companion.DEF
        }

        fun load(progress: Consumer<String?>) {
            progress.accept("loading basic images")
            Res.readData()
            progress.accept("loading enemies")
            loadEnemies()
            progress.accept("loading units")
            loadUnits()
            progress.accept("loading auxiliary data")
            Combo.Companion.readFile()
            PCoin.Companion.read()
            progress.accept("loading effects")
            EffAnim.Companion.read()
            progress.accept("loading backgrounds")
            Background.Companion.read()
            progress.accept("loading cat castles")
            NyCastle.Companion.read()
            progress.accept("loading souls")
            loadSoul()
            progress.accept("loading stages")
            DefMapColc.Companion.read()
            RandStage.read()
            loadCharaGroup()
            loadLimit()
            progress.accept("loading orbs")
            Orb.Companion.read()
            progress.accept("loading musics")
            loadMusic()
        }

        private fun loadCharaGroup() {
            val qs: Queue<String> = VFile.Companion.readLine("./org/data/Charagroup.csv")
            qs.poll()
            for (str in qs) {
                val strs = str.split(",").toTypedArray()
                val id: Int = CommonStatic.parseIntN(strs[0])
                val type: Int = CommonStatic.parseIntN(strs[2])
                val units: Array<common.pack.PackData.Identifier<Unit>> = arrayOfNulls<common.pack.PackData.Identifier<*>>(strs.size - 3)
                for (i in 3 until strs.size) units[i - 3] = common.pack.PackData.Identifier.Companion.parseInt<Unit>(CommonStatic.parseIntN(strs[i]), Unit::class.java)
                groups.set(id, CharaGroup(id, type, units))
            }
        }

        private fun loadEnemies() {
            VFile.Companion.get("./org/enemy/").list().forEach(Consumer<VFile<FileDesc?>> { p: VFile<FileDesc?>? -> enemies.add(Enemy(p)) })
            var qs: Queue<String> = VFile.Companion.readLine("./org/data/t_unit.csv")
            qs.poll()
            qs.poll()
            for (e in enemies.getList()) (e.de as DataEnemy).fillData(qs.poll().split("//").toTypedArray()[0].trim { it <= ' ' }.split(",").toTypedArray())
            qs = VFile.Companion.readLine("./org/data/enemy_dictionary_list.csv")
            for (str in qs) enemies.get(str.split(",").toTypedArray()[0].toInt()).inDic = true
        }

        private fun loadLimit() {
            val qs: Queue<String> = VFile.Companion.readLine("./org/data/Stage_option.csv")
            qs.poll()
            for (str in qs) DefLimit(str.split(",").toTypedArray())
        }

        private fun loadMusic() {
            val dict: File = CommonStatic.ctx.getAssetFile("./music/")
            if (!dict.exists()) return
            val fs = dict.listFiles()
            for (f in fs) {
                val str = f.name
                if (str.length != 7) continue
                if (!str.endsWith(".ogg")) continue
                val id: Int = CommonStatic.parseIntN(str.substring(0, 3))
                if (id == -1) continue
                musics.set(id, Music(common.pack.PackData.Identifier.Companion.parseInt<Music>(id, Music::class.java), FDFile(f)))
            }
        }

        private fun loadSoul() {
            val pre = "./org/battle/soul/"
            val mid = "/battle_soul_"
            for (i in 0..12) souls.add(Soul(pre + Data.Companion.trio(i) + mid + Data.Companion.trio(i), i))
        }

        private fun loadUnits() {
            VFile.Companion.get("./org/unit").list().forEach(Consumer<VFile<FileDesc?>> { p: VFile<FileDesc?>? -> units.add(Unit(p)) })
            var qs: Queue<String> = VFile.Companion.readLine("./org/data/unitlevel.csv")
            val lu: List<Unit> = units.getList()
            val l: FixIndexList<UnitLevel> = unitLevels
            for (u in lu) {
                val strs = qs.poll().split(",").toTypedArray()
                val lv = IntArray(20)
                for (i in 0..19) lv[i] = strs[i].toInt()
                val ul = UnitLevel(lv)
                if (!l.contains(ul)) {
                    ul.id = common.pack.PackData.Identifier<UnitLevel>(common.pack.PackData.Identifier.Companion.DEF, UnitLevel::class.java, l.size())
                    l.add(ul)
                }
                val ind: Int = l.indexOf(ul)
                u.lv = l.get(ind)
                l.get(ind).units.add(u)
            }
            UnitLevel.Companion.def = l.get(2)
            qs = VFile.Companion.readLine("./org/data/unitbuy.csv")
            for (u in lu) {
                val strs = qs.poll().split(",").toTypedArray()
                u.rarity = strs[13].toInt()
                u.max = strs[50].toInt()
                u.maxp = strs[51].toInt()
                u.info.fillBuy(strs)
            }
        }
    }

    @JsonClass(noTag = NoTag.LOAD)
    class Identifier<T : Indexable<*, T>?> : Comparable<common.pack.PackData.Identifier<*>?>, Cloneable {
        var cls: Class<out T>?
        var pack: String?
        var id: Int

        @Deprecated("")
        constructor() {
            cls = null
            pack = null
            id = 0
        }

        constructor(pack: String?, cls: Class<out T>?, id: Int) {
            this.cls = cls
            this.pack = pack
            this.id = id
        }

        public override fun clone(): common.pack.PackData.Identifier<T> {
            return Data.Companion.err<Any>(SupExc<Any> { super.clone() })
        }

        override operator fun compareTo(identifier: common.pack.PackData.Identifier<*>): Int {
            val `val` = pack!!.compareTo(identifier.pack)
            return if (`val` != 0) `val` else Integer.compare(id, identifier.id)
        }

        fun equals(o: common.pack.PackData.Identifier<T>): Boolean {
            return pack == o.pack && id == o.id
        }

        @Deprecated("")
        @JCGetter
        fun get(): T? {
            val cont: IndexContainer = getCont()
            return cont.getList<Any?>(cls, Reductor<Any, FixIndexMap<*>> { r: Any?, l: FixIndexMap<*> ->
                r ?: l.get(id)
            }, null) as T
        }

        fun getCont(): IndexContainer {
            return common.pack.PackData.Identifier.Companion.getContainer(cls, pack)
        }

        fun isNull(): Boolean {
            return id == -1
        }

        override fun toString(): String {
            return "$pack/$id"
        }

        companion object {
            const val DEF = "_default"
            const val STATIC_FIXER = "id_fixer"
            operator fun <T : Indexable<*, T>?> get(id: common.pack.PackData.Identifier<T>?): T? {
                return id?.get()
            }

            /**
             * cls must be a class implementing Indexable. interfaces or other classes will
             * go through fixer
             */
            fun <T : Indexable<*, T>?> parseInt(v: Int, cls: Class<out T>?): common.pack.PackData.Identifier<T> {
                return common.pack.PackData.Identifier.Companion.parseIntRaw(v, cls)
            }

            @Deprecated("")
            fun parseIntRaw(v: Int, cls: Class<*>?): common.pack.PackData.Identifier<*> {
                var cls = cls
                if (cls == null || cls.isInterface || !Indexable::class.java.isAssignableFrom(cls)) cls = UserProfile.Companion.getStatic<IdFixer>(common.pack.PackData.Identifier.Companion.STATIC_FIXER, Supplier { IdFixer(null) }).parse(v, cls)
                val pack: String = if (cls != CastleImg::class.java && v / 1000 == 0) common.pack.PackData.Identifier.Companion.DEF else Data.Companion.hex(v / 1000)
                val id = v % 1000
                return common.pack.PackData.Identifier(pack, cls, id)
            }

            private fun getContainer(cls: Class<*>, str: String): Any? {
                var cont: IndexCont? = null
                val q: Queue<Class<*>> = ArrayDeque()
                q.add(cls)
                while (q.size > 0) {
                    val ci = q.poll()
                    if (ci.getAnnotation(IndexCont::class.java).also { cont = it } != null) break
                    if (ci.superclass != null) q.add(ci.superclass)
                    for (cj in ci.interfaces) q.add(cj)
                }
                if (cont == null) return null
                var m: Method? = null
                for (mi in cont.value().getMethods()) if (mi.getAnnotation(ContGetter::class.java) != null) m = mi
                if (m == null) return null
                val fm: Method = m
                return Data.Companion.err<Any>(SupExc<Any> { fm.invoke(null, str) })
            }
        }
    }

    @JsonClass(noTag = NoTag.LOAD)
    class PackDesc {
        var BCU_VERSION: String? = null
        var id: String? = null
        var author: String? = null
        var name: String? = null
        var desc: String? = null
        var time: String? = null
        var version = 0

        @JsonField(generic = [String::class])
        var dependency: ArrayList<String>? = null

        @JCConstructor
        @Deprecated("")
        constructor() {
        }

        constructor(id: String?) {
            BCU_VERSION = AssetLoader.CORE_VER
            this.id = id
            dependency = ArrayList()
        }
    }

    @JsonClass(read = RType.FILL)
    class UserPack : PackData {
        @JsonField
        val desc: PackDesc

        @JsonField(gen = GenType.FILL)
        var mc: PackMapColc? = null

        @JsonField(gen = GenType.FILL)
        var castles: PackCasList? = null
        val source: Source
        var editable = false
        var loaded = false
        private var elem: JsonElement? = null

        /** for old reading method only  */
        @Deprecated("")
        constructor(desc: PackDesc, s: Source) {
            this.desc = desc
            source = s
        }

        constructor(s: Source, desc: PackDesc, elem: JsonElement?) {
            this.desc = desc
            this.elem = elem
            source = s
            editable = source is Workspace
            mc = PackMapColc(this)
        }

        /** for generating new pack only  */
        constructor(id: String?) {
            desc = PackDesc(id)
            source = Workspace(id)
            castles = PackCasList(this)
            mc = PackMapColc(this)
            editable = true
            loaded = true
        }

        fun delete() {
            // FIXME Auto-generated method stub
        }

        fun forceRemoveParent(id: String?) {
            // FIXME Auto-generated method stub
        }

        override fun getID(): String? {
            return desc.id
        }

        fun <T : Indexable<*, T>?> getID(cls: Class<T>?, id: Int): common.pack.PackData.Identifier<T> {
            return common.pack.PackData.Identifier(desc.id, cls, id)
        }

        fun getReplays(): Collection<Recd>? {
            // FIXME Auto-generated method stub
            return null
        }

        fun loadMusics() {
            // FIXME
        }

        fun relyOn(id: String?): Boolean {
            // FIXME
            return false
        }

        @Throws(Exception::class)
        fun load() {
            JsonDecoder.Companion.inject<UserPack>(elem, UserPack::class.java, this)
            elem = null
            loaded = true
            loadMusics()
        }
    }

    val enemies: FixIndexMap<Enemy> = FixIndexMap<Enemy>(Enemy::class.java)
    val randEnemies: FixIndexMap<EneRand> = FixIndexMap<EneRand>(EneRand::class.java)
    val units: FixIndexMap<Unit> = FixIndexMap<Unit>(Unit::class.java)
    val unitLevels: FixIndexMap<UnitLevel> = FixIndexMap<UnitLevel>(UnitLevel::class.java)
    val souls: FixIndexMap<Soul> = FixIndexMap<Soul>(Soul::class.java)
    val bgs: FixIndexMap<Background> = FixIndexMap<Background>(Background::class.java)
    val groups: FixIndexMap<CharaGroup> = FixIndexMap<CharaGroup>(CharaGroup::class.java)
    val lvrs: FixIndexMap<LvRestrict> = FixIndexMap<LvRestrict>(LvRestrict::class.java)
    val musics: FixIndexMap<Music> = FixIndexMap<Music>(Music::class.java)
    override fun <R> getList(cls: Class<*>, func: Reductor<R, FixIndexMap<*>?>, def: R): R {
        var def = def
        if (cls == Unit::class.java) def = func.reduce(def, units)
        if (cls == Enemy::class.java || cls == AbEnemy::class.java) def = func.reduce(def, enemies)
        if (cls == EneRand::class.java || cls == AbEnemy::class.java) def = func.reduce(def, randEnemies)
        if (cls == Background::class.java) def = func.reduce(def, bgs)
        if (cls == Soul::class.java) def = func.reduce(def, souls)
        if (cls == Music::class.java) def = func.reduce(def, musics)
        if (cls == CharaGroup::class.java) def = func.reduce(def, groups)
        if (cls == LvRestrict::class.java) def = func.reduce(def, lvrs)
        return def
    }

    companion object {
        @ContGetter
        fun getPack(str: String?): PackData {
            return UserProfile.Companion.getPack(str)
        }
    }
}