package common.util.stageimport

import common.pack.UserProfile
import common.system.files.FileData
import common.util.Data
import common.util.stage.Stage
import java.util.*

com.google.api.client.json.jackson2.JacksonFactoryimport com.google.api.services.drive.DriveScopesimport com.google.api.client.util.store.FileDataStoreFactoryimport com.google.api.client.http.HttpTransportimport com.google.api.services.drive.Driveimport kotlin.Throwsimport java.io.IOExceptionimport io.drive.DriveUtilimport java.io.FileNotFoundExceptionimport java.io.FileInputStreamimport com.google.api.client.googleapis.auth.oauth2.GoogleClientSecretsimport java.io.InputStreamReaderimport com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlowimport com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledAppimport com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiverimport com.google.api.client.googleapis.javanet.GoogleNetHttpTransportimport kotlin.jvm.JvmStaticimport io.drive.DrvieInitimport com.google.api.client.http.javanet.NetHttpTransportimport com.google.api.services.drive.model.FileListimport java.io.BufferedInputStreamimport java.io.FileOutputStreamimport com.google.api.client.googleapis.media.MediaHttpDownloaderimport io.WebFileIOimport io.BCJSONimport page.LoadPageimport org.json.JSONObjectimport org.json.JSONArrayimport main.MainBCUimport main.Optsimport common.CommonStaticimport java.util.TreeMapimport java.util.Arraysimport java.io.BufferedReaderimport io.BCMusicimport common.util.stage.Musicimport io.BCPlayerimport java.util.HashMapimport javax.sound.sampled.Clipimport java.io.ByteArrayInputStreamimport javax.sound.sampled.AudioInputStreamimport javax.sound.sampled.AudioSystemimport javax.sound.sampled.DataLineimport javax.sound.sampled.FloatControlimport javax.sound.sampled.LineEventimport com.google.api.client.googleapis.media.MediaHttpDownloaderProgressListenerimport com.google.api.client.googleapis.media.MediaHttpDownloader.DownloadStateimport common.io.DataIOimport io.BCUReaderimport common.io.InStreamimport com.google.gson.JsonElementimport common.io.json.JsonDecoderimport com.google.gson.JsonObjectimport page.MainFrameimport page.view.ViewBox.Confimport page.MainLocaleimport page.battle.BattleInfoPageimport page.support.Exporterimport page.support.Importerimport common.pack.Context.ErrTypeimport common.util.stage.MapColcimport common.util.stage.MapColc.DefMapColcimport common.util.lang.MultiLangContimport common.util.stage.StageMapimport common.util.unit.Enemyimport io.BCUWriterimport java.text.SimpleDateFormatimport java.io.PrintStreamimport common.io.OutStreamimport common.battle.BasisSetimport res.AnimatedGifEncoderimport java.awt.image.BufferedImageimport javax.imageio.ImageIOimport java.security.MessageDigestimport java.security.NoSuchAlgorithmExceptionimport common.io.json.JsonEncoderimport java.io.FileWriterimport com.google.api.client.http.GenericUrlimport org.apache.http.impl .client.CloseableHttpClientimport org.apache.http.impl .client.HttpClientsimport org.apache.http.client.methods.HttpPostimport org.apache.http.entity.mime.content.FileBodyimport org.apache.http.entity.mime.MultipartEntityBuilderimport org.apache.http.entity.mime.HttpMultipartModeimport org.apache.http.HttpEntityimport org.apache.http.util.EntityUtilsimport com.google.api.client.http.HttpRequestInitializerimport com.google.api.client.http.HttpBackOffUnsuccessfulResponseHandlerimport com.google.api.client.util.ExponentialBackOffimport com.google.api.client.http.HttpBackOffIOExceptionHandlerimport res.NeuQuantimport res.LZWEncoderimport java.io.BufferedOutputStreamimport java.awt.Graphics2Dimport java.awt.image.DataBufferByteimport common.system.fake.FakeImageimport utilpc.awt.FIBIimport jogl.util.AmbImageimport common.system.files.VFileimport jogl.util.GLImageimport com.jogamp.opengl.util.texture.TextureDataimport common.system.Pimport com.jogamp.opengl.util.texture.TextureIOimport jogl.GLStaticimport com.jogamp.opengl.util.texture.awt.AWTTextureIOimport java.awt.AlphaCompositeimport common.system.fake.FakeImage.Markerimport jogl.util.GLGraphicsimport com.jogamp.opengl.GL2import jogl.util.GeoAutoimport com.jogamp.opengl.GL2ES3import com.jogamp.opengl.GLimport common.system.fake.FakeGraphicsimport common.system.fake.FakeTransformimport jogl.util.ResManagerimport jogl.util.GLGraphics.GeomGimport jogl.util.GLGraphics.GLCimport jogl.util.GLGraphics.GLTimport com.jogamp.opengl.GL2ES2import com.jogamp.opengl.util.glsl.ShaderCodeimport com.jogamp.opengl.util.glsl.ShaderProgramimport com.jogamp.opengl.GLExceptionimport jogl.StdGLCimport jogl.Tempimport common.util.anim.AnimUimport common.util.anim.EAnimUimport jogl.util.GLIBimport javax.swing.JFrameimport common.util.anim.AnimCEimport common.util.anim.AnimU.UTypeimport com.jogamp.opengl.util.FPSAnimatorimport com.jogamp.opengl.GLEventListenerimport com.jogamp.opengl.GLAutoDrawableimport page.awt.BBBuilderimport page.battle.BattleBox.OuterBoximport common.battle.SBCtrlimport page.battle.BattleBoximport jogl.GLBattleBoximport common.battle.BattleFieldimport page.anim.IconBoximport jogl.GLIconBoximport jogl.GLBBRecdimport page.awt.RecdThreadimport page.view.ViewBoximport jogl.GLViewBoximport page.view.ViewBox.Controllerimport java.awt.AWTExceptionimport page.battle.BBRecdimport jogl.GLRecorderimport com.jogamp.opengl.GLProfileimport com.jogamp.opengl.GLCapabilitiesimport page.anim.IconBox.IBCtrlimport page.anim.IconBox.IBConfimport page.view.ViewBox.VBExporterimport jogl.GLRecdBImgimport page.JTGimport jogl.GLCstdimport jogl.GLVBExporterimport common.util.anim.EAnimIimport page.RetFuncimport page.battle.BattleBox.BBPainterimport page.battle.BBCtrlimport javax.swing.JOptionPaneimport kotlin.jvm.Strictfpimport main.Invimport javax.swing.SwingUtilitiesimport java.lang.InterruptedExceptionimport utilpc.UtilPC.PCItrimport utilpc.awt.PCIBimport jogl.GLBBBimport page.awt.AWTBBBimport utilpc.Themeimport page.MainPageimport common.io.assets.AssetLoaderimport common.pack.Source.Workspaceimport common.io.PackLoader.ZipDesc.FileDescimport common.io.assets.Adminimport page.awt.BattleBoxDefimport page.awt.IconBoxDefimport page.awt.BBRecdAWTimport page.awt.ViewBoxDefimport org.jcodec.api.awt.AWTSequenceEncoderimport page.awt.RecdThread.PNGThreadimport page.awt.RecdThread.MP4Threadimport page.awt.RecdThread.GIFThreadimport java.awt.GradientPaintimport utilpc.awt.FG2Dimport page.anim.TreeContimport javax.swing.JTreeimport javax.swing.event.TreeExpansionListenerimport common.util.anim.MaModelimport javax.swing.tree.DefaultMutableTreeNodeimport javax.swing.event.TreeExpansionEventimport java.util.function.IntPredicateimport javax.swing.tree.DefaultTreeModelimport common.util.anim.EAnimDimport page.anim.AnimBoximport utilpc.PPimport common.CommonStatic.BCAuxAssetsimport common.CommonStatic.EditLinkimport page.JBTNimport page.anim.DIYViewPageimport page.anim.ImgCutEditPageimport page.anim.MaModelEditPageimport page.anim.MaAnimEditPageimport page.anim.EditHeadimport java.awt.event.ActionListenerimport page.anim.AbEditPageimport common.util.anim.EAnimSimport page.anim.ModelBoximport common.util.anim.ImgCutimport page.view.AbViewPageimport javax.swing.JListimport javax.swing.JScrollPaneimport javax.swing.JComboBoximport utilpc.UtilPCimport javax.swing.event.ListSelectionListenerimport javax.swing.event.ListSelectionEventimport common.system.VImgimport page.support.AnimLCRimport page.support.AnimTableimport common.util.anim.MaAnimimport java.util.EventObjectimport javax.swing.text.JTextComponentimport page.anim.PartEditTableimport javax.swing.ListSelectionModelimport page.support.AnimTableTHimport page.JTFimport utilpc.ReColorimport page.anim.ImgCutEditTableimport page.anim.SpriteBoximport page.anim.SpriteEditPageimport java.awt.event.FocusAdapterimport java.awt.event.FocusEventimport common.pack.PackData.UserPackimport utilpc.Algorithm.SRResultimport page.anim.MaAnimEditTableimport javax.swing.JSliderimport java.awt.event.MouseWheelEventimport common.util.anim.EPartimport javax.swing.event.ChangeEventimport page.anim.AdvAnimEditPageimport javax.swing.BorderFactoryimport page.JLimport javax.swing.ImageIconimport page.anim.MMTreeimport javax.swing.event.TreeSelectionListenerimport javax.swing.event.TreeSelectionEventimport page.support.AbJTableimport page.anim.MaModelEditTableimport page.info.edit.ProcTableimport page.info.edit.ProcTable.AtkProcTableimport page.info.edit.SwingEditorimport page.info.edit.ProcTable.MainProcTableimport page.support.ListJtfPolicyimport page.info.edit.SwingEditor.SwingEGimport common.util.Data.Procimport java.lang.Runnableimport javax.swing.JComponentimport page.info.edit.LimitTableimport page.pack.CharaGroupPageimport page.pack.LvRestrictPageimport javax.swing.SwingConstantsimport common.util.lang.Editors.EditorGroupimport common.util.lang.Editors.EdiFieldimport common.util.lang.Editorsimport common.util.lang.ProcLangimport page.info.edit.EntityEditPageimport common.util.lang.Editors.EditorSupplierimport common.util.lang.Editors.EditControlimport page.info.edit.SwingEditor.IntEditorimport page.info.edit.SwingEditor.BoolEditorimport page.info.edit.SwingEditor.IdEditorimport page.SupPageimport common.util.unit.AbEnemyimport common.pack.IndexContainer.Indexableimport common.pack.Context.SupExcimport common.battle.data .AtkDataModelimport utilpc.Interpretimport common.battle.data .CustomEntityimport page.info.filter.UnitEditBoximport common.battle.data .CustomUnitimport common.util.stage.SCGroupimport page.info.edit.SCGroupEditTableimport common.util.stage.SCDefimport page.info.filter.EnemyEditBoximport common.battle.data .CustomEnemyimport page.info.StageFilterPageimport page.view.BGViewPageimport page.view.CastleViewPageimport page.view.MusicPageimport common.util.stage.CastleImgimport common.util.stage.CastleListimport java.text.DecimalFormatimport common.util.stage.Recdimport common.util.stage.MapColc.PackMapColcimport page.info.edit.StageEditTableimport page.support.ReorderListimport page.info.edit.HeadEditTableimport page.info.filter.EnemyFindPageimport page.battle.BattleSetupPageimport page.info.edit.AdvStEditPageimport page.battle.StRecdPageimport page.info.edit.LimitEditPageimport page.support.ReorderListenerimport common.util.pack.Soulimport page.info.edit.AtkEditTableimport page.info.filter.UnitFindPageimport common.battle.Basisimport common.util.Data.Proc.IMUimport javax.swing.DefaultComboBoxModelimport common.util.Animableimport common.util.pack.Soul.SoulTypeimport page.view.UnitViewPageimport page.view.EnemyViewPageimport page.info.edit.SwingEditor.EditCtrlimport page.support.Reorderableimport page.info.EnemyInfoPageimport common.util.unit.EneRandimport page.pack.EREditPageimport page.support.InTableTHimport page.support.EnemyTCRimport javax.swing.DefaultListCellRendererimport page.info.filter.UnitListTableimport page.info.filter.UnitFilterBoximport page.info.filter.EnemyListTableimport page.info.filter.EnemyFilterBoximport page.info.filter.UFBButtonimport page.info.filter.UFBListimport common.battle.data .MaskUnitimport javax.swing.AbstractButtonimport page.support.SortTableimport page.info.UnitInfoPageimport page.support.UnitTCRimport page.info.filter.EFBButtonimport page.info.filter.EFBListimport common.util.stage.LvRestrictimport common.util.stage.CharaGroupimport page.info.StageTableimport page.info.TreaTableimport javax.swing.JPanelimport page.info.UnitInfoTableimport page.basis.BasisPageimport kotlin.jvm.JvmOverloadsimport page.info.EnemyInfoTableimport common.util.stage.RandStageimport page.info.StagePageimport page.info.StageRandPageimport common.util.unit.EFormimport page.pack.EREditTableimport common.util.EREntimport common.pack.FixIndexListimport page.support.UnitLCRimport page.pack.RecdPackPageimport page.pack.CastleEditPageimport page.pack.BGEditPageimport page.pack.CGLREditPageimport common.pack.Source.ZipSourceimport page.info.edit.EnemyEditPageimport page.info.edit.StageEditPageimport page.info.StageViewPageimport page.pack.UnitManagePageimport page.pack.MusicEditPageimport page.battle.AbRecdPageimport common.system.files.VFileRootimport java.awt.Desktopimport common.pack.PackDataimport common.util.unit.UnitLevelimport page.info.edit.FormEditPageimport common.util.anim.AnimIimport common.util.anim.AnimI.AnimTypeimport common.util.anim.AnimDimport common.battle.data .Orbimport page.basis.LineUpBoximport page.basis.LubContimport common.battle.BasisLUimport page.basis.ComboListTableimport page.basis.ComboListimport page.basis.NyCasBoximport page.basis.UnitFLUPageimport common.util.unit.Comboimport page.basis.LevelEditPageimport common.util.pack.NyCastleimport common.battle.LineUpimport common.system.SymCoordimport java.util.TreeSetimport page.basis.OrbBoximport javax.swing.table.DefaultTableCellRendererimport javax.swing.JTableimport common.CommonStatic.BattleConstimport common.battle.StageBasisimport common.util.ImgCoreimport common.battle.attack.ContAbimport common.battle.entity.EAnimContimport common.battle.entity.WaprContimport page.battle.RecdManagePageimport page.battle.ComingTableimport common.util.stage.EStageimport page.battle.EntityTableimport common.battle.data .MaskEnemyimport common.battle.SBRplyimport common.battle.entity.AbEntityimport page.battle.RecdSavePageimport page.LocCompimport page.LocSubCompimport javax.swing.table.TableModelimport page.support.TModelimport javax.swing.event.TableModelListenerimport javax.swing.table.DefaultTableColumnModelimport javax.swing.JFileChooserimport javax.swing.filechooser.FileNameExtensionFilterimport javax.swing.TransferHandlerimport java.awt.datatransfer.Transferableimport java.awt.datatransfer.DataFlavorimport javax.swing.DropModeimport javax.swing.TransferHandler.TransferSupportimport java.awt.dnd.DragSourceimport java.awt.datatransfer.UnsupportedFlavorExceptionimport common.system.Copableimport page.support.AnimTransferimport javax.swing.DefaultListModelimport page.support.InListTHimport java.awt.FocusTraversalPolicyimport javax.swing.JTextFieldimport page.CustomCompimport javax.swing.JToggleButtonimport javax.swing.JButtonimport javax.swing.ToolTipManagerimport javax.swing.JRootPaneimport javax.swing.JProgressBarimport page.ConfigPageimport page.view.EffectViewPageimport page.pack.PackEditPageimport page.pack.ResourcePageimport javax.swing.WindowConstantsimport java.awt.event.AWTEventListenerimport java.awt.AWTEventimport java.awt.event.ComponentAdapterimport java.awt.event.ComponentEventimport java.util.ConcurrentModificationExceptionimport javax.swing.plaf.FontUIResourceimport java.util.Enumerationimport javax.swing.UIManagerimport common.CommonStatic.FakeKeyimport page.LocSubComp.LocBinderimport page.LSCPopimport java.awt.BorderLayoutimport java.awt.GridLayoutimport javax.swing.JTextPaneimport page.TTTimport java.util.ResourceBundleimport java.util.MissingResourceExceptionimport java.util.Localeimport common.io.json.Test.JsonTest_2import common.pack.PackData.PackDescimport common.io.PackLoaderimport common.io.PackLoader.Preloadimport common.io.PackLoader.ZipDescimport common.io.json.Testimport common.io.json.JsonClassimport common.io.json.JsonFieldimport common.io.json.JsonField.GenTypeimport common.io.json.Test.JsonTest_0.JsonDimport common.io.json.JsonClass.RTypeimport java.util.HashSetimport common.io.json.JsonDecoder.OnInjectedimport common.io.json.JsonField.IOTypeimport common.io.json.JsonExceptionimport common.io.json.JsonClass.NoTagimport common.io.json.JsonField.SerTypeimport common.io.json.JsonClass.WTypeimport kotlin.reflect.KClassimport com.google.gson.JsonArrayimport common.io.assets.Admin.StaticPermittedimport common.io.json.JsonClass.JCGenericimport common.io.json.JsonClass.JCGetterimport com.google.gson.JsonPrimitiveimport com.google.gson.JsonNullimport common.io.json.JsonClass.JCIdentifierimport java.lang.ClassNotFoundExceptionimport common.io.assets.AssetLoader.AssetHeaderimport common.io.assets.AssetLoader.AssetHeader.AssetEntryimport common.io.InStreamDefimport common.io.BCUExceptionimport java.io.UnsupportedEncodingExceptionimport common.io.OutStreamDefimport javax.crypto.Cipherimport javax.crypto.spec.IvParameterSpecimport javax.crypto.spec.SecretKeySpecimport common.io.PackLoader.FileSaverimport common.system.files.FDByteimport common.io.json.JsonClass.JCConstructorimport common.io.PackLoader.FileLoader.FLStreamimport common.io.PackLoader.PatchFileimport java.lang.NullPointerExceptionimport java.lang.IndexOutOfBoundsExceptionimport common.io.MultiStreamimport java.io.RandomAccessFileimport common.io.MultiStream.TrueStreamimport java.lang.RuntimeExceptionimport common.pack.Source.ResourceLocationimport common.pack.Source.AnimLoaderimport common.pack.Source.SourceAnimLoaderimport common.util.anim.AnimCIimport common.system.files.FDFileimport common.pack.IndexContainerimport common.battle.data .PCoinimport common.util.pack.EffAnimimport common.battle.data .DataEnemyimport common.util.stage.Limit.DefLimitimport common.pack.IndexContainer.Reductorimport common.pack.FixIndexList.FixIndexMapimport common.pack.VerFixer.IdFixerimport common.pack.IndexContainer.IndexContimport common.pack.IndexContainer.ContGetterimport common.util.stage.CastleList.PackCasListimport common.util.Data.Proc.THEMEimport common.CommonStatic.ImgReaderimport common.pack.VerFixerimport common.pack.VerFixer.VerFixerExceptionimport java.lang.NumberFormatExceptionimport common.pack.Source.SourceAnimSaverimport common.pack.VerFixer.EnemyFixerimport common.pack.VerFixer.PackFixerimport common.pack.PackData.DefPackimport java.util.function.BiConsumerimport common.util.BattleStaticimport common.util.anim.AnimU.ImageKeeperimport common.util.anim.AnimCE.AnimCELoaderimport common.util.anim.AnimCI.AnimCIKeeperimport common.util.anim.AnimUD.DefImgLoaderimport common.util.BattleObjimport common.util.Data.Proc.ProcItemimport common.util.lang.ProcLang.ItemLangimport common.util.lang.LocaleCenter.Displayableimport common.util.lang.Editors.DispItemimport common.util.lang.LocaleCenter.ObjBinderimport common.util.lang.LocaleCenter.ObjBinder.BinderFuncimport common.util.Data.Proc.PROBimport org.jcodec.common.tools.MathUtilimport common.util.Data.Proc.PTimport common.util.Data.Proc.PTDimport common.util.Data.Proc.PMimport common.util.Data.Proc.WAVEimport common.util.Data.Proc.WEAKimport common.util.Data.Proc.STRONGimport common.util.Data.Proc.BURROWimport common.util.Data.Proc.REVIVEimport common.util.Data.Proc.SUMMONimport common.util.Data.Proc.MOVEWAVEimport common.util.Data.Proc.POISONimport common.util.Data.Proc.CRITIimport common.util.Data.Proc.VOLCimport common.util.Data.Proc.ARMORimport common.util.Data.Proc.SPEEDimport java.util.LinkedHashMapimport common.util.lang.LocaleCenter.DisplayItemimport common.util.lang.ProcLang.ProcLangStoreimport common.util.lang.Formatter.IntExpimport common.util.lang.Formatter.RefObjimport common.util.lang.Formatter.BoolExpimport common.util.lang.Formatter.BoolElemimport common.util.lang.Formatter.IElemimport common.util.lang.Formatter.Contimport common.util.lang.Formatter.Elemimport common.util.lang.Formatter.RefElemimport common.util.lang.Formatter.RefFieldimport common.util.lang.Formatter.RefFuncimport common.util.lang.Formatter.TextRefimport common.util.lang.Formatter.CodeBlockimport common.util.lang.Formatter.TextPlainimport common.util.unit.Unit.UnitInfoimport common.util.lang.MultiLangCont.MultiLangStaticsimport common.util.pack.EffAnim.EffTypeimport common.util.pack.EffAnim.ArmorEffimport common.util.pack.EffAnim.BarEneEffimport common.util.pack.EffAnim.BarrierEffimport common.util.pack.EffAnim.DefEffimport common.util.pack.EffAnim.WarpEffimport common.util.pack.EffAnim.ZombieEffimport common.util.pack.EffAnim.KBEffimport common.util.pack.EffAnim.SniperEffimport common.util.pack.EffAnim.VolcEffimport common.util.pack.EffAnim.SpeedEffimport common.util.pack.EffAnim.WeakUpEffimport common.util.pack.EffAnim.EffAnimStoreimport common.util.pack.NyCastle.NyTypeimport common.util.pack.WaveAnimimport common.util.pack.WaveAnim.WaveTypeimport common.util.pack.Background.BGWvTypeimport common.util.unit.Form.FormJsonimport common.system.BasedCopableimport common.util.anim.AnimUDimport common.battle.data .DataUnitimport common.battle.entity.EUnitimport common.battle.entity.EEnemyimport common.util.EntRandimport common.util.stage.Recd.Waitimport java.lang.CloneNotSupportedExceptionimport common.util.stage.StageMap.StageMapInfoimport common.util.stage.Stage.StageInfoimport common.util.stage.Limit.PackLimitimport common.util.stage.MapColc.ClipMapColcimport common.util.stage.CastleList.DefCasListimport common.util.stage.MapColc.StItrimport common.util.Data.Proc.IntType.BitCountimport common.util.CopRandimport common.util.LockGLimport java.lang.IllegalAccessExceptionimport common.battle.data .MaskAtkimport common.battle.data .DefaultDataimport common.battle.data .DataAtkimport common.battle.data .MaskEntityimport common.battle.data .DataEntityimport common.battle.attack.AtkModelAbimport common.battle.attack.AttackAbimport common.battle.attack.AttackSimpleimport common.battle.attack.AttackWaveimport common.battle.entity.Cannonimport common.battle.attack.AttackVolcanoimport common.battle.attack.ContWaveAbimport common.battle.attack.ContWaveDefimport common.battle.attack.AtkModelEntityimport common.battle.entity.EntContimport common.battle.attack.ContMoveimport common.battle.attack.ContVolcanoimport common.battle.attack.ContWaveCanonimport common.battle.attack.AtkModelEnemyimport common.battle.attack.AtkModelUnitimport common.battle.attack.AttackCanonimport common.battle.entity.EUnit.OrbHandlerimport common.battle.entity.Entity.AnimManagerimport common.battle.entity.Entity.AtkManagerimport common.battle.entity.Entity.ZombXimport common.battle.entity.Entity.KBManagerimport common.battle.entity.Entity.PoisonTokenimport common.battle.entity.Entity.WeakTokenimport common.battle.Treasureimport common.battle.MirrorSetimport common.battle.Releaseimport common.battle.ELineUpimport common.battle.entity.Sniperimport common.battle.entity.ECastleimport java.util.Dequeimport common.CommonStatic.Itfimport java.lang.Characterimport common.CommonStatic.ImgWriterimport utilpc.awt.FTATimport utilpc.awt.Blenderimport java.awt.RenderingHintsimport utilpc.awt.BIBuilderimport java.awt.CompositeContextimport java.awt.image.Rasterimport java.awt.image.WritableRasterimport utilpc.ColorSetimport utilpc.OggTimeReaderimport utilpc.UtilPC.PCItr.MusicReaderimport utilpc.UtilPC.PCItr.PCALimport javax.swing.UIManager.LookAndFeelInfoimport java.lang.InstantiationExceptionimport javax.swing.UnsupportedLookAndFeelExceptionimport utilpc.Algorithm.ColorShiftimport utilpc.Algorithm.StackRect
@JsonClass(read = RType.FILL)
open class MapColc : Data() {
    class DefMapColc : MapColc {
        val id: Int
        val name: String

        private constructor() {
            id = 3
            UserProfile.Companion.getRegister<MapColc>(REG_MAPCOLC, MapColc::class.java).put(Data.Companion.trio(id), this)
            name = "CH"
            maps = arrayOfNulls<StageMap>(14)
            val abbr = "./org/stage/CH/stageNormal/stageNormal"
            for (i in 0..2) {
                var vf: FileData = VFile.Companion.get(abbr + "0_" + i + "_Z.csv").getData()
                maps[i] = StageMap(this, i, vf, 1)
                maps[i].name = "EoC " + (i + 1) + " Zombie"
                vf = VFile.Companion.get(abbr + "1_" + i + ".csv").getData()
                maps[3 + i] = StageMap(this, 3 + i, vf, 2)
                maps[i + 3].name = "ItF " + (i + 1)
                vf = VFile.Companion.get(abbr + "2_" + i + ".csv").getData()
                maps[6 + i] = StageMap(this, 6 + i, vf, 3)
                maps[i + 6].name = "CotC " + (i + 1)
            }
            var stn: FileData = VFile.Companion.get(abbr + "0.csv").getData()
            maps[9] = StageMap(this, 9, stn, 1)
            maps[9].name = "EoC 1-3"
            stn = VFile.Companion.get(abbr + "1_0_Z.csv").getData()
            maps[10] = StageMap(this, 10, stn, 2)
            maps[10].name = "ItF 1 Zombie"
            stn = VFile.Companion.get(abbr + "2_2_Invasion.csv").getData()
            maps[11] = StageMap(this, 11, stn, 2)
            maps[11].name = "CotC 3 Invasion"
            stn = VFile.Companion.get(abbr + "1_1_Z.csv").getData()
            maps[12] = StageMap(this, 12, stn, 2)
            maps[12].name = "ItF 2 Zombie"
            maps[13] = StageMap(this, 13, stn, 2)
            maps[13].name = "ItF 3 Zombie"
            val stz: VFile<*> = VFile.Companion.get("./org/stage/CH/stageZ/")
            for (vf in stz.list()) {
                val str: String = vf.getName()
                var id0 = -1
                var id1 = -1
                try {
                    id0 = str.substring(6, 8).toInt()
                    id1 = str.substring(9, 11).toInt()
                } catch (e: Exception) {
                    e.printStackTrace()
                    continue
                }
                if (id0 < 3) maps[id0].add(Stage(maps[id0], id1, vf, 0)) else if (id0 == 4) maps[10].add(Stage(maps[10], id1, vf, 0)) else if (id0 == 5) maps[12].add(Stage(maps[12], id1, vf, 0)) else if (id0 == 6) maps[13].add(Stage(maps[13], id1, vf, 0))
            }
            val stw: VFile<*> = VFile.Companion.get("./org/stage/CH/stageW/")
            for (vf in stw.list()) {
                val str: String = vf.getName()
                var id0 = -1
                var id1 = -1
                try {
                    id0 = str.substring(6, 8).toInt()
                    id1 = str.substring(9, 11).toInt()
                } catch (e: Exception) {
                    e.printStackTrace()
                    continue
                }
                maps[id0 - 1].add(Stage(maps[id0 - 1], id1, vf, 1))
            }
            val sts: VFile<*> = VFile.Companion.get("./org/stage/CH/stageSpace/")
            for (vf in sts.list()) {
                val str: String = vf.getName()
                if (str.length > 20) {
                    maps[11].add(Stage(maps[11], 0, vf, 0))
                    continue
                }
                var id0 = -1
                var id1 = -1
                try {
                    id0 = str.substring(10, 12).toInt()
                    id1 = str.substring(13, 15).toInt()
                } catch (e: Exception) {
                    e.printStackTrace()
                    continue
                }
                maps[id0 - 1].add(Stage(maps[id0 - 1], id1, vf, 1))
            }
            val st: VFile<*> = VFile.Companion.get("./org/stage/CH/stage/")
            for (vf in st.list()) {
                val str: String = vf.getName()
                var id0 = -1
                id0 = try {
                    str.substring(5, 7).toInt()
                } catch (e: Exception) {
                    e.printStackTrace()
                    continue
                }
                maps[9].add(Stage(maps[9], id0, vf, 2))
            }
            maps[9].stars = intArrayOf(100, 200, 400)
        }

        private constructor(st: String, ID: Int, stage: List<VFile<*>>, map: VFile<*>) {
            name = st
            id = ID
            UserProfile.Companion.getRegister<MapColc>(REG_MAPCOLC, MapColc::class.java).put(Data.Companion.trio(id), this)
            val sms: Array<StageMap?> = arrayOfNulls<StageMap>(map.list().size)
            for (m in map.list()) {
                val str: String = m.getName()
                val len = str.length
                var id = -1
                id = try {
                    str.substring(len - 7, len - 4).toInt()
                } catch (e: Exception) {
                    e.printStackTrace()
                    continue
                }
                sms[id] = StageMap(this, id, m.getData())
            }
            maps = sms
            for (s in stage) {
                val str: String = s.getName()
                val len = str.length
                var id0 = -1
                var id1 = -1
                try {
                    id0 = str.substring(len - 10, len - 7).toInt()
                    id1 = str.substring(len - 6, len - 4).toInt()
                } catch (e: Exception) {
                    e.printStackTrace()
                    continue
                }
                sms[id0]!!.add(Stage(sms[id0], id1, s, 0))
            }
        }

        override fun toString(): String {
            val desp: String = MultiLangCont.Companion.get(this)
            return if (desp != null && desp.length > 0) desp + " (" + maps.size + ")" else name + " (" + maps.size + ")"
        }

        companion object {
            private const val REG_IDMAP = "DefMapColc_idmap"

            /** get a BC stage  */
            fun getMap(mid: Int): StageMap? {
                val map: Map<String, MapColc> = UserProfile.Companion.getRegister<MapColc>(REG_MAPCOLC, MapColc::class.java)
                val mc = map[Data.Companion.trio(mid / 1000)] ?: return null
                return mc.maps[mid % 1000]
            }

            fun getMap(id: String?): DefMapColc {
                return UserProfile.Companion.getRegister<MapColc>(REG_MAPCOLC, MapColc::class.java)
                        .get(Data.Companion.trio(UserProfile.Companion.getRegister<Int>(REG_IDMAP, Int::class.java).get(id)))
            }

            fun read() {
                val idmap: MutableMap<String, Int> = UserProfile.Companion.getRegister<Int>(REG_IDMAP, Int::class.java)
                idmap["E"] = 4
                idmap["N"] = 0
                idmap["S"] = 1
                idmap["C"] = 2
                idmap["CH"] = 3
                idmap["T"] = 6
                idmap["V"] = 7
                idmap["R"] = 11
                idmap["M"] = 12
                idmap["A"] = 13
                idmap["B"] = 14
                idmap["RA"] = 24
                idmap["H"] = 25
                idmap["CA"] = 27
                for (i in strs.indices) DefCasList(Data.Companion.hex(i), strs[i])
                val f: VFile<*> = VFile.Companion.get("./org/stage/") ?: return
                for (fi in f.list()) {
                    if (fi.getName() == "CH") continue
                    if (fi.getName() == "D") continue
                    val list: List<VFile<*>> = ArrayList<VFile<*>>(fi.list())
                    val map: VFile<*> = list[0]
                    val stage: MutableList<VFile<*>> = ArrayList<VFile<*>>()
                    for (i in 1 until list.size) if (list[i].list() != null) stage.addAll(list[i].list())
                    DefMapColc(fi.getName(), idmap[fi.getName()]!!, stage, map)
                }
                DefMapColc()
                val qs: Queue<String> = VFile.Companion.readLine("./org/data/Map_option.csv")
                qs.poll()
                for (str in qs) {
                    val strs = str.trim { it <= ' ' }.split(",").toTypedArray()
                    val id = strs[0].toInt()
                    val sm: StageMap = getMap(id) ?: continue
                    val len = strs[1].toInt()
                    sm.stars = IntArray(len)
                    for (i in 0 until len) sm.stars.get(i) = strs[2 + i].toInt()
                    sm.set = strs[6].toInt()
                    sm.retyp = strs[7].toInt()
                    sm.pllim = strs[8].toInt()
                    sm.name += strs[10]
                }
            }
        }
    }

    @JsonClass
    class PackMapColc : MapColc {
        val pack: UserPack

        constructor(pack: UserPack) {
            this.pack = pack
        }

        @Deprecated("")
        constructor(pack: UserPack, `is`: InStream) {
            this.pack = pack
            val `val`: Int = Data.Companion.getVer(`is`.nextString())
            if (`val` != 308) throw VerFixerException("MapColc requires 308, got $`val`")
            `is`.nextString()
            var n: Int = `is`.nextInt()
            for (i in 0 until n) {
                val cg = CharaGroup(pack, `is`)
                pack.groups.set(cg.id!!.id, cg)
            }
            n = `is`.nextInt()
            for (i in 0 until n) {
                val lr = LvRestrict(pack, `is`)
                pack.lvrs.set(lr.id!!.id, lr)
            }
            n = `is`.nextInt()
            maps = arrayOfNulls<StageMap>(n)
            for (i in 0 until n) {
                val sm = StageMap(this)
                maps[i] = sm
                sm.name = `is`.nextString()
                sm.stars = `is`.nextIntsB()
                var m: Int = `is`.nextInt()
                for (j in 0 until m) {
                    val sub: InStream = `is`.subStream()
                    sm.add(Stage(pack, sm, sub))
                }
                m = `is`.nextInt()
                for (j in 0 until m) sm.lim.add(PackLimit(pack, `is`))
            }
        }

        override fun toString(): String {
            return pack.desc.name
        }
    }

    class StItr : MutableIterator<Stage?>, Iterable<Stage?> {
        private var imc: Iterator<MapColc>?
        private var mc: MapColc
        private var ism: Int
        private var `is`: Int
        override fun hasNext(): Boolean {
            return imc != null
        }

        override fun iterator(): MutableIterator<Stage> {
            return this
        }

        override fun next(): Stage {
            val ans: Stage = mc.maps[ism]!!.list.get(`is`)
            `is`++
            validate()
            return ans
        }

        private fun validate() {
            while (`is` >= mc.maps[ism]!!.list.size) {
                `is` = 0
                ism++
                while (ism >= mc.maps.size) {
                    ism = 0
                    if (!imc!!.hasNext()) {
                        imc = null
                        return
                    }
                    mc = imc!!.next()
                }
            }
        }

        init {
            imc = UserProfile.Companion.getRegister<MapColc>(REG_MAPCOLC, MapColc::class.java).values.iterator()
            mc = imc!!.next()
            `is` = 0
            ism = `is`
            validate()
        }
    }

    class ClipMapColc : MapColc() {
        override fun toString(): String {
            return "clipboard"
        }

        init {
            maps = arrayOfNulls<StageMap>(1)
            maps[0] = StageMap(this)
        }
    }

    @JsonField
    var maps: Array<StageMap?> = arrayOfNulls<StageMap>(0)

    companion object {
        private const val REG_MAPCOLC = "MapColc"

        @StaticPermitted
        private val strs = arrayOf("rc", "ec", "sc", "wc")
        fun getAllStage(): Iterable<Stage> {
            return StItr()
        }

        fun values(): Collection<MapColc> {
            return UserProfile.Companion.getRegister<MapColc>(REG_MAPCOLC, MapColc::class.java).values
        }
    }
}