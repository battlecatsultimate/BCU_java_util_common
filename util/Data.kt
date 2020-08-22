package common.utilimport

import common.pack.Context
import common.pack.PackData
import common.util.Data
import common.util.pack.Background
import java.lang.annotation.Documented
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy

com.google.api.client.json.jackson2.JacksonFactoryimport com.google.api.services.drive.DriveScopesimport com.google.api.client.util.store.FileDataStoreFactoryimport com.google.api.client.http.HttpTransportimport com.google.api.services.drive.Driveimport kotlin.Throwsimport java.io.IOExceptionimport io.drive.DriveUtilimport java.io.FileNotFoundExceptionimport java.io.FileInputStreamimport com.google.api.client.googleapis.auth.oauth2.GoogleClientSecretsimport java.io.InputStreamReaderimport com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlowimport com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledAppimport com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiverimport com.google.api.client.googleapis.javanet.GoogleNetHttpTransportimport kotlin.jvm.JvmStaticimport io.drive.DrvieInitimport com.google.api.client.http.javanet.NetHttpTransportimport com.google.api.services.drive.model.FileListimport java.io.BufferedInputStreamimport java.io.FileOutputStreamimport com.google.api.client.googleapis.media.MediaHttpDownloaderimport io.WebFileIOimport io.BCJSONimport page.LoadPageimport org.json.JSONObjectimport org.json.JSONArrayimport main.MainBCUimport main.Optsimport common.CommonStaticimport java.util.TreeMapimport java.util.Arraysimport java.io.BufferedReaderimport io.BCMusicimport common.util.stage.Musicimport io.BCPlayerimport java.util.HashMapimport javax.sound.sampled.Clipimport java.io.ByteArrayInputStreamimport javax.sound.sampled.AudioInputStreamimport javax.sound.sampled.AudioSystemimport javax.sound.sampled.DataLineimport javax.sound.sampled.FloatControlimport javax.sound.sampled.LineEventimport com.google.api.client.googleapis.media.MediaHttpDownloaderProgressListenerimport com.google.api.client.googleapis.media.MediaHttpDownloader.DownloadStateimport common.io.DataIOimport io.BCUReaderimport common.io.InStreamimport com.google.gson.JsonElementimport common.io.json.JsonDecoderimport com.google.gson.JsonObjectimport page.MainFrameimport page.view.ViewBox.Confimport page.MainLocaleimport page.battle.BattleInfoPageimport page.support.Exporterimport page.support.Importerimport common.pack.Context.ErrTypeimport common.util.stage.MapColcimport common.util.stage.MapColc.DefMapColcimport common.util.lang.MultiLangContimport common.util.stage.StageMapimport common.util.unit.Enemyimport io.BCUWriterimport java.text.SimpleDateFormatimport java.io.PrintStreamimport common.io.OutStreamimport common.battle.BasisSetimport res.AnimatedGifEncoderimport java.awt.image.BufferedImageimport javax.imageio.ImageIOimport java.security.MessageDigestimport java.security.NoSuchAlgorithmExceptionimport common.io.json.JsonEncoderimport java.io.FileWriterimport com.google.api.client.http.GenericUrlimport org.apache.http.impl .client.CloseableHttpClientimport org.apache.http.impl .client.HttpClientsimport org.apache.http.client.methods.HttpPostimport org.apache.http.entity.mime.content.FileBodyimport org.apache.http.entity.mime.MultipartEntityBuilderimport org.apache.http.entity.mime.HttpMultipartModeimport org.apache.http.HttpEntityimport org.apache.http.util.EntityUtilsimport com.google.api.client.http.HttpRequestInitializerimport com.google.api.client.http.HttpBackOffUnsuccessfulResponseHandlerimport com.google.api.client.util.ExponentialBackOffimport com.google.api.client.http.HttpBackOffIOExceptionHandlerimport res.NeuQuantimport res.LZWEncoderimport java.io.BufferedOutputStreamimport java.awt.Graphics2Dimport java.awt.image.DataBufferByteimport common.system.fake.FakeImageimport utilpc.awt.FIBIimport jogl.util.AmbImageimport common.system.files.VFileimport jogl.util.GLImageimport com.jogamp.opengl.util.texture.TextureDataimport common.system.Pimport com.jogamp.opengl.util.texture.TextureIOimport jogl.GLStaticimport com.jogamp.opengl.util.texture.awt.AWTTextureIOimport java.awt.AlphaCompositeimport common.system.fake.FakeImage.Markerimport jogl.util.GLGraphicsimport com.jogamp.opengl.GL2import jogl.util.GeoAutoimport com.jogamp.opengl.GL2ES3import com.jogamp.opengl.GLimport common.system.fake.FakeGraphicsimport common.system.fake.FakeTransformimport jogl.util.ResManagerimport jogl.util.GLGraphics.GeomGimport jogl.util.GLGraphics.GLCimport jogl.util.GLGraphics.GLTimport com.jogamp.opengl.GL2ES2import com.jogamp.opengl.util.glsl.ShaderCodeimport com.jogamp.opengl.util.glsl.ShaderProgramimport com.jogamp.opengl.GLExceptionimport jogl.StdGLCimport jogl.Tempimport common.util.anim.AnimUimport common.util.anim.EAnimUimport jogl.util.GLIBimport javax.swing.JFrameimport common.util.anim.AnimCEimport common.util.anim.AnimU.UTypeimport com.jogamp.opengl.util.FPSAnimatorimport com.jogamp.opengl.GLEventListenerimport com.jogamp.opengl.GLAutoDrawableimport page.awt.BBBuilderimport page.battle.BattleBox.OuterBoximport common.battle.SBCtrlimport page.battle.BattleBoximport jogl.GLBattleBoximport common.battle.BattleFieldimport page.anim.IconBoximport jogl.GLIconBoximport jogl.GLBBRecdimport page.awt.RecdThreadimport page.view.ViewBoximport jogl.GLViewBoximport page.view.ViewBox.Controllerimport java.awt.AWTExceptionimport page.battle.BBRecdimport jogl.GLRecorderimport com.jogamp.opengl.GLProfileimport com.jogamp.opengl.GLCapabilitiesimport page.anim.IconBox.IBCtrlimport page.anim.IconBox.IBConfimport page.view.ViewBox.VBExporterimport jogl.GLRecdBImgimport page.JTGimport jogl.GLCstdimport jogl.GLVBExporterimport common.util.anim.EAnimIimport page.RetFuncimport page.battle.BattleBox.BBPainterimport page.battle.BBCtrlimport javax.swing.JOptionPaneimport kotlin.jvm.Strictfpimport main.Invimport javax.swing.SwingUtilitiesimport java.lang.InterruptedExceptionimport utilpc.UtilPC.PCItrimport utilpc.awt.PCIBimport jogl.GLBBBimport page.awt.AWTBBBimport utilpc.Themeimport page.MainPageimport common.io.assets.AssetLoaderimport common.pack.Source.Workspaceimport common.io.PackLoader.ZipDesc.FileDescimport common.io.assets.Adminimport page.awt.BattleBoxDefimport page.awt.IconBoxDefimport page.awt.BBRecdAWTimport page.awt.ViewBoxDefimport org.jcodec.api.awt.AWTSequenceEncoderimport page.awt.RecdThread.PNGThreadimport page.awt.RecdThread.MP4Threadimport page.awt.RecdThread.GIFThreadimport java.awt.GradientPaintimport utilpc.awt.FG2Dimport page.anim.TreeContimport javax.swing.JTreeimport javax.swing.event.TreeExpansionListenerimport common.util.anim.MaModelimport javax.swing.tree.DefaultMutableTreeNodeimport javax.swing.event.TreeExpansionEventimport java.util.function.IntPredicateimport javax.swing.tree.DefaultTreeModelimport common.util.anim.EAnimDimport page.anim.AnimBoximport utilpc.PPimport common.CommonStatic.BCAuxAssetsimport common.CommonStatic.EditLinkimport page.JBTNimport page.anim.DIYViewPageimport page.anim.ImgCutEditPageimport page.anim.MaModelEditPageimport page.anim.MaAnimEditPageimport page.anim.EditHeadimport java.awt.event.ActionListenerimport page.anim.AbEditPageimport common.util.anim.EAnimSimport page.anim.ModelBoximport common.util.anim.ImgCutimport page.view.AbViewPageimport javax.swing.JListimport javax.swing.JScrollPaneimport javax.swing.JComboBoximport utilpc.UtilPCimport javax.swing.event.ListSelectionListenerimport javax.swing.event.ListSelectionEventimport common.system.VImgimport page.support.AnimLCRimport page.support.AnimTableimport common.util.anim.MaAnimimport java.util.EventObjectimport javax.swing.text.JTextComponentimport page.anim.PartEditTableimport javax.swing.ListSelectionModelimport page.support.AnimTableTHimport page.JTFimport utilpc.ReColorimport page.anim.ImgCutEditTableimport page.anim.SpriteBoximport page.anim.SpriteEditPageimport java.awt.event.FocusAdapterimport java.awt.event.FocusEventimport common.pack.PackData.UserPackimport utilpc.Algorithm.SRResultimport page.anim.MaAnimEditTableimport javax.swing.JSliderimport java.awt.event.MouseWheelEventimport common.util.anim.EPartimport javax.swing.event.ChangeEventimport page.anim.AdvAnimEditPageimport javax.swing.BorderFactoryimport page.JLimport javax.swing.ImageIconimport page.anim.MMTreeimport javax.swing.event.TreeSelectionListenerimport javax.swing.event.TreeSelectionEventimport page.support.AbJTableimport page.anim.MaModelEditTableimport page.info.edit.ProcTableimport page.info.edit.ProcTable.AtkProcTableimport page.info.edit.SwingEditorimport page.info.edit.ProcTable.MainProcTableimport page.support.ListJtfPolicyimport page.info.edit.SwingEditor.SwingEGimport common.util.Data.Procimport java.lang.Runnableimport javax.swing.JComponentimport page.info.edit.LimitTableimport page.pack.CharaGroupPageimport page.pack.LvRestrictPageimport javax.swing.SwingConstantsimport common.util.lang.Editors.EditorGroupimport common.util.lang.Editors.EdiFieldimport common.util.lang.Editorsimport common.util.lang.ProcLangimport page.info.edit.EntityEditPageimport common.util.lang.Editors.EditorSupplierimport common.util.lang.Editors.EditControlimport page.info.edit.SwingEditor.IntEditorimport page.info.edit.SwingEditor.BoolEditorimport page.info.edit.SwingEditor.IdEditorimport page.SupPageimport common.util.unit.AbEnemyimport common.pack.IndexContainer.Indexableimport common.pack.Context.SupExcimport common.battle.data .AtkDataModelimport utilpc.Interpretimport common.battle.data .CustomEntityimport page.info.filter.UnitEditBoximport common.battle.data .CustomUnitimport common.util.stage.SCGroupimport page.info.edit.SCGroupEditTableimport common.util.stage.SCDefimport page.info.filter.EnemyEditBoximport common.battle.data .CustomEnemyimport page.info.StageFilterPageimport page.view.BGViewPageimport page.view.CastleViewPageimport page.view.MusicPageimport common.util.stage.CastleImgimport common.util.stage.CastleListimport java.text.DecimalFormatimport common.util.stage.Recdimport common.util.stage.MapColc.PackMapColcimport page.info.edit.StageEditTableimport page.support.ReorderListimport page.info.edit.HeadEditTableimport page.info.filter.EnemyFindPageimport page.battle.BattleSetupPageimport page.info.edit.AdvStEditPageimport page.battle.StRecdPageimport page.info.edit.LimitEditPageimport page.support.ReorderListenerimport common.util.pack.Soulimport page.info.edit.AtkEditTableimport page.info.filter.UnitFindPageimport common.battle.Basisimport common.util.Data.Proc.IMUimport javax.swing.DefaultComboBoxModelimport common.util.Animableimport common.util.pack.Soul.SoulTypeimport page.view.UnitViewPageimport page.view.EnemyViewPageimport page.info.edit.SwingEditor.EditCtrlimport page.support.Reorderableimport page.info.EnemyInfoPageimport common.util.unit.EneRandimport page.pack.EREditPageimport page.support.InTableTHimport page.support.EnemyTCRimport javax.swing.DefaultListCellRendererimport page.info.filter.UnitListTableimport page.info.filter.UnitFilterBoximport page.info.filter.EnemyListTableimport page.info.filter.EnemyFilterBoximport page.info.filter.UFBButtonimport page.info.filter.UFBListimport common.battle.data .MaskUnitimport javax.swing.AbstractButtonimport page.support.SortTableimport page.info.UnitInfoPageimport page.support.UnitTCRimport page.info.filter.EFBButtonimport page.info.filter.EFBListimport common.util.stage.LvRestrictimport common.util.stage.CharaGroupimport page.info.StageTableimport page.info.TreaTableimport javax.swing.JPanelimport page.info.UnitInfoTableimport page.basis.BasisPageimport kotlin.jvm.JvmOverloadsimport page.info.EnemyInfoTableimport common.util.stage.RandStageimport page.info.StagePageimport page.info.StageRandPageimport common.util.unit.EFormimport page.pack.EREditTableimport common.util.EREntimport common.pack.FixIndexListimport page.support.UnitLCRimport page.pack.RecdPackPageimport page.pack.CastleEditPageimport page.pack.BGEditPageimport page.pack.CGLREditPageimport common.pack.Source.ZipSourceimport page.info.edit.EnemyEditPageimport page.info.edit.StageEditPageimport page.info.StageViewPageimport page.pack.UnitManagePageimport page.pack.MusicEditPageimport page.battle.AbRecdPageimport common.system.files.VFileRootimport java.awt.Desktopimport common.pack.PackDataimport common.util.unit.UnitLevelimport page.info.edit.FormEditPageimport common.util.anim.AnimIimport common.util.anim.AnimI.AnimTypeimport common.util.anim.AnimDimport common.battle.data .Orbimport page.basis.LineUpBoximport page.basis.LubContimport common.battle.BasisLUimport page.basis.ComboListTableimport page.basis.ComboListimport page.basis.NyCasBoximport page.basis.UnitFLUPageimport common.util.unit.Comboimport page.basis.LevelEditPageimport common.util.pack.NyCastleimport common.battle.LineUpimport common.system.SymCoordimport java.util.TreeSetimport page.basis.OrbBoximport javax.swing.table.DefaultTableCellRendererimport javax.swing.JTableimport common.CommonStatic.BattleConstimport common.battle.StageBasisimport common.util.ImgCoreimport common.battle.attack.ContAbimport common.battle.entity.EAnimContimport common.battle.entity.WaprContimport page.battle.RecdManagePageimport page.battle.ComingTableimport common.util.stage.EStageimport page.battle.EntityTableimport common.battle.data .MaskEnemyimport common.battle.SBRplyimport common.battle.entity.AbEntityimport page.battle.RecdSavePageimport page.LocCompimport page.LocSubCompimport javax.swing.table.TableModelimport page.support.TModelimport javax.swing.event.TableModelListenerimport javax.swing.table.DefaultTableColumnModelimport javax.swing.JFileChooserimport javax.swing.filechooser.FileNameExtensionFilterimport javax.swing.TransferHandlerimport java.awt.datatransfer.Transferableimport java.awt.datatransfer.DataFlavorimport javax.swing.DropModeimport javax.swing.TransferHandler.TransferSupportimport java.awt.dnd.DragSourceimport java.awt.datatransfer.UnsupportedFlavorExceptionimport common.system.Copableimport page.support.AnimTransferimport javax.swing.DefaultListModelimport page.support.InListTHimport java.awt.FocusTraversalPolicyimport javax.swing.JTextFieldimport page.CustomCompimport javax.swing.JToggleButtonimport javax.swing.JButtonimport javax.swing.ToolTipManagerimport javax.swing.JRootPaneimport javax.swing.JProgressBarimport page.ConfigPageimport page.view.EffectViewPageimport page.pack.PackEditPageimport page.pack.ResourcePageimport javax.swing.WindowConstantsimport java.awt.event.AWTEventListenerimport java.awt.AWTEventimport java.awt.event.ComponentAdapterimport java.awt.event.ComponentEventimport java.util.ConcurrentModificationExceptionimport javax.swing.plaf.FontUIResourceimport java.util.Enumerationimport javax.swing.UIManagerimport common.CommonStatic.FakeKeyimport page.LocSubComp.LocBinderimport page.LSCPopimport java.awt.BorderLayoutimport java.awt.GridLayoutimport javax.swing.JTextPaneimport page.TTTimport java.util.ResourceBundleimport java.util.MissingResourceExceptionimport java.util.Localeimport common.io.json.Test.JsonTest_2import common.pack.PackData.PackDescimport common.io.PackLoaderimport common.io.PackLoader.Preloadimport common.io.PackLoader.ZipDescimport common.io.json.Testimport common.io.json.JsonClassimport common.io.json.JsonFieldimport common.io.json.JsonField.GenTypeimport common.io.json.Test.JsonTest_0.JsonDimport common.io.json.JsonClass.RTypeimport java.util.HashSetimport common.io.json.JsonDecoder.OnInjectedimport common.io.json.JsonField.IOTypeimport common.io.json.JsonExceptionimport common.io.json.JsonClass.NoTagimport common.io.json.JsonField.SerTypeimport common.io.json.JsonClass.WTypeimport kotlin.reflect.KClassimport com.google.gson.JsonArrayimport common.io.assets.Admin.StaticPermittedimport common.io.json.JsonClass.JCGenericimport common.io.json.JsonClass.JCGetterimport com.google.gson.JsonPrimitiveimport com.google.gson.JsonNullimport common.io.json.JsonClass.JCIdentifierimport java.lang.ClassNotFoundExceptionimport common.io.assets.AssetLoader.AssetHeaderimport common.io.assets.AssetLoader.AssetHeader.AssetEntryimport common.io.InStreamDefimport common.io.BCUExceptionimport java.io.UnsupportedEncodingExceptionimport common.io.OutStreamDefimport javax.crypto.Cipherimport javax.crypto.spec.IvParameterSpecimport javax.crypto.spec.SecretKeySpecimport common.io.PackLoader.FileSaverimport common.system.files.FDByteimport common.io.json.JsonClass.JCConstructorimport common.io.PackLoader.FileLoader.FLStreamimport common.io.PackLoader.PatchFileimport java.lang.NullPointerExceptionimport java.lang.IndexOutOfBoundsExceptionimport common.io.MultiStreamimport java.io.RandomAccessFileimport common.io.MultiStream.TrueStreamimport java.lang.RuntimeExceptionimport common.pack.Source.ResourceLocationimport common.pack.Source.AnimLoaderimport common.pack.Source.SourceAnimLoaderimport common.util.anim.AnimCIimport common.system.files.FDFileimport common.pack.IndexContainerimport common.battle.data .PCoinimport common.util.pack.EffAnimimport common.battle.data .DataEnemyimport common.util.stage.Limit.DefLimitimport common.pack.IndexContainer.Reductorimport common.pack.FixIndexList.FixIndexMapimport common.pack.VerFixer.IdFixerimport common.pack.IndexContainer.IndexContimport common.pack.IndexContainer.ContGetterimport common.util.stage.CastleList.PackCasListimport common.util.Data.Proc.THEMEimport common.CommonStatic.ImgReaderimport common.pack.VerFixerimport common.pack.VerFixer.VerFixerExceptionimport java.lang.NumberFormatExceptionimport common.pack.Source.SourceAnimSaverimport common.pack.VerFixer.EnemyFixerimport common.pack.VerFixer.PackFixerimport common.pack.PackData.DefPackimport java.util.function.BiConsumerimport common.util.BattleStaticimport common.util.anim.AnimU.ImageKeeperimport common.util.anim.AnimCE.AnimCELoaderimport common.util.anim.AnimCI.AnimCIKeeperimport common.util.anim.AnimUD.DefImgLoaderimport common.util.BattleObjimport common.util.Data.Proc.ProcItemimport common.util.lang.ProcLang.ItemLangimport common.util.lang.LocaleCenter.Displayableimport common.util.lang.Editors.DispItemimport common.util.lang.LocaleCenter.ObjBinderimport common.util.lang.LocaleCenter.ObjBinder.BinderFuncimport common.util.Data.Proc.PROBimport org.jcodec.common.tools.MathUtilimport common.util.Data.Proc.PTimport common.util.Data.Proc.PTDimport common.util.Data.Proc.PMimport common.util.Data.Proc.WAVEimport common.util.Data.Proc.WEAKimport common.util.Data.Proc.STRONGimport common.util.Data.Proc.BURROWimport common.util.Data.Proc.REVIVEimport common.util.Data.Proc.SUMMONimport common.util.Data.Proc.MOVEWAVEimport common.util.Data.Proc.POISONimport common.util.Data.Proc.CRITIimport common.util.Data.Proc.VOLCimport common.util.Data.Proc.ARMORimport common.util.Data.Proc.SPEEDimport java.util.LinkedHashMapimport common.util.lang.LocaleCenter.DisplayItemimport common.util.lang.ProcLang.ProcLangStoreimport common.util.lang.Formatter.IntExpimport common.util.lang.Formatter.RefObjimport common.util.lang.Formatter.BoolExpimport common.util.lang.Formatter.BoolElemimport common.util.lang.Formatter.IElemimport common.util.lang.Formatter.Contimport common.util.lang.Formatter.Elemimport common.util.lang.Formatter.RefElemimport common.util.lang.Formatter.RefFieldimport common.util.lang.Formatter.RefFuncimport common.util.lang.Formatter.TextRefimport common.util.lang.Formatter.CodeBlockimport common.util.lang.Formatter.TextPlainimport common.util.unit.Unit.UnitInfoimport common.util.lang.MultiLangCont.MultiLangStaticsimport common.util.pack.EffAnim.EffTypeimport common.util.pack.EffAnim.ArmorEffimport common.util.pack.EffAnim.BarEneEffimport common.util.pack.EffAnim.BarrierEffimport common.util.pack.EffAnim.DefEffimport common.util.pack.EffAnim.WarpEffimport common.util.pack.EffAnim.ZombieEffimport common.util.pack.EffAnim.KBEffimport common.util.pack.EffAnim.SniperEffimport common.util.pack.EffAnim.VolcEffimport common.util.pack.EffAnim.SpeedEffimport common.util.pack.EffAnim.WeakUpEffimport common.util.pack.EffAnim.EffAnimStoreimport common.util.pack.NyCastle.NyTypeimport common.util.pack.WaveAnimimport common.util.pack.WaveAnim.WaveTypeimport common.util.pack.Background.BGWvTypeimport common.util.unit.Form.FormJsonimport common.system.BasedCopableimport common.util.anim.AnimUDimport common.battle.data .DataUnitimport common.battle.entity.EUnitimport common.battle.entity.EEnemyimport common.util.EntRandimport common.util.stage.Recd.Waitimport java.lang.CloneNotSupportedExceptionimport common.util.stage.StageMap.StageMapInfoimport common.util.stage.Stage.StageInfoimport common.util.stage.Limit.PackLimitimport common.util.stage.MapColc.ClipMapColcimport common.util.stage.CastleList.DefCasListimport common.util.stage.MapColc.StItrimport common.util.Data.Proc.IntType.BitCountimport common.util.CopRandimport common.util.LockGLimport java.lang.IllegalAccessExceptionimport common.battle.data .MaskAtkimport common.battle.data .DefaultDataimport common.battle.data .DataAtkimport common.battle.data .MaskEntityimport common.battle.data .DataEntityimport common.battle.attack.AtkModelAbimport common.battle.attack.AttackAbimport common.battle.attack.AttackSimpleimport common.battle.attack.AttackWaveimport common.battle.entity.Cannonimport common.battle.attack.AttackVolcanoimport common.battle.attack.ContWaveAbimport common.battle.attack.ContWaveDefimport common.battle.attack.AtkModelEntityimport common.battle.entity.EntContimport common.battle.attack.ContMoveimport common.battle.attack.ContVolcanoimport common.battle.attack.ContWaveCanonimport common.battle.attack.AtkModelEnemyimport common.battle.attack.AtkModelUnitimport common.battle.attack.AttackCanonimport common.battle.entity.EUnit.OrbHandlerimport common.battle.entity.Entity.AnimManagerimport common.battle.entity.Entity.AtkManagerimport common.battle.entity.Entity.ZombXimport common.battle.entity.Entity.KBManagerimport common.battle.entity.Entity.PoisonTokenimport common.battle.entity.Entity.WeakTokenimport common.battle.Treasureimport common.battle.MirrorSetimport common.battle.Releaseimport common.battle.ELineUpimport common.battle.entity.Sniperimport common.battle.entity.ECastleimport java.util.Dequeimport common.CommonStatic.Itfimport java.lang.Characterimport common.CommonStatic.ImgWriterimport utilpc.awt.FTATimport utilpc.awt.Blenderimport java.awt.RenderingHintsimport utilpc.awt.BIBuilderimport java.awt.CompositeContextimport java.awt.image.Rasterimport java.awt.image.WritableRasterimport utilpc.ColorSetimport utilpc.OggTimeReaderimport utilpc.UtilPC.PCItr.MusicReaderimport utilpc.UtilPC.PCItr.PCALimport javax.swing.UIManager.LookAndFeelInfoimport java.lang.InstantiationExceptionimport javax.swing.UnsupportedLookAndFeelExceptionimport utilpc.Algorithm.ColorShiftimport utilpc.Algorithm.StackRect
@StaticPermitted
open class Data {
    @JsonClass(noTag = NoTag.LOAD)
    class Proc {
        @JsonClass(noTag = NoTag.LOAD)
        class ARMOR : ProcItem() {
            var prob = 0
            var time = 0
            var mult = 0
        }

        @JsonClass(noTag = NoTag.LOAD)
        class BURROW : ProcItem() {
            var count = 0
            var dis = 0
        }

        @JsonClass(noTag = NoTag.LOAD)
        class CRITI : ProcItem() {
            var type = 0
        }

        @JsonClass(noTag = NoTag.LOAD)
        class IMU : ProcItem() {
            var mult = 0
        }

        abstract class IntType : Cloneable {
            @Documented
            @Retention(value = RetentionPolicy.RUNTIME)
            @Target(AnnotationTarget.FIELD)
            annotation class BitCount(val value: Int)

            @Throws(CloneNotSupportedException::class)
            public override fun clone(): Data.Proc.IntType {
                return super.clone() as Data.Proc.IntType
            }

            @Throws(Exception::class)
            fun load(`val`: Int): Data.Proc.IntType {
                val fs = this.javaClass.declaredFields
                var i = 0
                while (i < fs.size) {
                    val c = fs[i].getAnnotation(BitCount::class.java)
                    if (c == null) {
                        fs[i][this] = `val` shr i and 1 == 1
                        i++
                    } else {
                        fs[i][this] = `val` shr i and (1 shl c.value()) - 1
                        i += c.value()
                    }
                }
                return this
            }

            @Throws(Exception::class)
            fun toInt(): Int {
                val fs = this.javaClass.declaredFields
                var ans = 0
                var i = 0
                while (i < fs.size) {
                    val c = fs[i].getAnnotation(BitCount::class.java)
                    if (c == null) {
                        if (fs[i].getBoolean(this)) ans = ans or (1 shl i)
                        i++
                    } else {
                        val `val` = fs[i].getInt(this)
                        ans = ans or (`val` shl i)
                        i += c.value()
                    }
                }
                return ans
            }
        }

        @JsonClass(noTag = NoTag.LOAD)
        class MOVEWAVE : ProcItem() {
            var prob = 0
            var speed = 0
            var width = 0
            var time = 0
            var dis = 0
            var itv = 0
        }

        @JsonClass(noTag = NoTag.LOAD)
        class PM : ProcItem() {
            var prob = 0
            var mult = 0
        }

        @JsonClass(noTag = NoTag.LOAD)
        class POISON : ProcItem() {
            @JsonClass(noTag = NoTag.LOAD)
            class TYPE : Data.Proc.IntType() {
                @BitCount(2)
                var damage_type = 0
                var unstackable = false
            }

            var prob = 0
            var time = 0
            var damage = 0
            var itv = 0
            var type: Data.Proc.POISON.TYPE? = null
        }

        @JsonClass(noTag = NoTag.LOAD)
        class PROB : ProcItem() {
            var prob = 0
        }

        abstract class ProcItem : Cloneable {
            fun clear(): ProcItem {
                try {
                    val fs = this.javaClass.declaredFields
                    for (f in fs) if (f.type == Int::class.javaPrimitiveType) f[this] = 0 else if (Data.Proc.IntType::class.java.isAssignableFrom(f.type)) f[this] = f.type.newInstance() else if (f.type == PackData.Identifier::class.java) f[this] = null else throw Exception("unknown field " + f.type + " " + f.name)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                return this
            }

            public override fun clone(): ProcItem {
                return try {
                    val ans = super.clone() as ProcItem
                    val fs = this.javaClass.declaredFields
                    for (f in fs) if (Data.Proc.IntType::class.java.isAssignableFrom(f.type)) f[ans] = (f[this] as Data.Proc.IntType).clone() else if (f.type == PackData.Identifier::class.java && f[this] != null) f[ans] = (f[this] as PackData.Identifier<*>).clone()
                    ans
                } catch (e: Exception) {
                    e.printStackTrace()
                    null
                }
            }

            fun exists(): Boolean {
                try {
                    val fs = this.javaClass.declaredFields
                    for (f in fs) if (f.type == Int::class.javaPrimitiveType) {
                        val o = f[this]
                        if (o as Int != 0) return true
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                return false
            }

            @Deprecated("")
            operator fun get(i: Int): Int {
                return try {
                    val f = this.javaClass.declaredFields[i]
                    if (f.type == Int::class.javaPrimitiveType) f.getInt(this) else (f[this] as Data.Proc.IntType).toInt()
                } catch (e: Exception) {
                    e.printStackTrace()
                    0
                }
            }

            @Deprecated("")
            @Throws(Exception::class)
            fun load(data: IntArray): ProcItem {
                val fs = this.javaClass.declaredFields
                for (i in 0 until Math.min(data.size, fs.size)) if (fs[i].type == Int::class.javaPrimitiveType) fs[i][this] = data[i] else if (Data.Proc.IntType::class.java.isAssignableFrom(fs[i].type)) fs[i][this] = (fs[i].type.newInstance() as Data.Proc.IntType).load(data[i]) else if (fs[i].type == PackData.Identifier::class.java) fs[i][this] = PackData.Identifier.Companion.parseIntRaw(data[i], this.javaClass) else throw Exception("unknown field " + fs[i].type + " " + fs[i].name)
                return this
            }

            fun perform(r: CopRand): Boolean {
                return try {
                    val f = this.javaClass.getDeclaredField("prob") ?: return exists()
                    val prob = f.getInt(this)
                    if (prob == 0) false else r.nextDouble() * 100 < prob
                } catch (e: Exception) {
                    exists()
                }
            }

            /** should not modify IntType and Identifier  */
            @Deprecated("")
            operator fun set(i: Int, v: Int) {
                try {
                    val f = this.javaClass.declaredFields[i]
                    if (f.type == Int::class.javaPrimitiveType) f[this] = v
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            fun set(pi: ProcItem?) {
                try {
                    val fs = this.javaClass.declaredFields
                    for (f in fs) if (f.type.isPrimitive) f[this] = f[pi] else if (Data.Proc.IntType::class.java.isAssignableFrom(f.type)) f[this] = (f[pi] as Data.Proc.IntType).clone() else if (f.type == PackData.Identifier::class.java) f[this] = (f[pi] as PackData.Identifier<*>).clone() else throw Exception("unknown field " + f.type + " " + f.name)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            override fun toString(): String {
                return JsonEncoder.Companion.encode(this).toString()
            }
        }

        @JsonClass(noTag = NoTag.LOAD)
        class PT : ProcItem() {
            var prob = 0
            var time = 0
        }

        @JsonClass(noTag = NoTag.LOAD)
        class PTD : ProcItem() {
            var prob = 0
            var time = 0
            var dis = 0
        }

        @JsonClass(noTag = NoTag.LOAD)
        class REVIVE : ProcItem() {
            @JsonClass(noTag = NoTag.LOAD)
            class TYPE : Data.Proc.IntType() {
                @BitCount(2)
                var range_type = 0
                var imu_zkill = false
                var revive_non_zombie = false
                var revive_others = false
            }

            var count = 0
            var time = 0
            var health = 0
            var dis_0 = 0
            var dis_1 = 0
            var type: Data.Proc.REVIVE.TYPE? = null
        }

        @JsonClass(noTag = NoTag.LOAD)
        class SPEED : ProcItem() {
            var prob = 0
            var time = 0
            var speed = 0
            var type = 0
        }

        @JsonClass(noTag = NoTag.LOAD)
        class STRONG : ProcItem() {
            var health = 0
            var mult = 0
        }

        @JsonClass(noTag = NoTag.LOAD)
        class SUMMON : ProcItem() {
            @JsonClass(noTag = NoTag.LOAD)
            class TYPE : Data.Proc.IntType() {
                @BitCount(2)
                var anim_type = 0
                var ignore_limit = false
                var fix_buff = false
                var same_health = false
                var random_layer = false
                var on_hit = false
                var on_kill = false
            }

            var prob = 0
            var id: PackData.Identifier<*>? = null
            var dis = 0
            var mult = 0
            var type: Data.Proc.SUMMON.TYPE? = null
            var time = 0
        }

        @JsonClass(noTag = NoTag.LOAD)
        class THEME : ProcItem() {
            @JsonClass(noTag = NoTag.LOAD)
            class TYPE : Data.Proc.IntType() {
                var kill = false
            }

            var prob = 0
            var time = 0
            var id: PackData.Identifier<Background>? = null
            var type: Data.Proc.THEME.TYPE? = null
        }

        @JsonClass(noTag = NoTag.LOAD)
        class VOLC : ProcItem() {
            var prob = 0
            var dis_0 = 0
            var dis_1 = 0
            var time = 0
        }

        @JsonClass(noTag = NoTag.LOAD)
        class WAVE : ProcItem() {
            var prob = 0
            var lv = 0
        }

        @JsonClass(noTag = NoTag.LOAD)
        class WEAK : ProcItem() {
            var prob = 0
            var time = 0
            var mult = 0
        }

        val KB: PTD? = null
        val STOP: PT? = null
        val SLOW: PT? = null
        val CRIT: PM? = null
        val WAVE: WAVE? = null
        val WEAK: WEAK? = null
        val BREAK: PROB? = null
        val WARP: PTD? = null
        val CURSE: PT? = null
        val STRONG: STRONG? = null
        val LETHAL: PROB? = null
        val BURROW: BURROW? = null
        val REVIVE: REVIVE? = null
        val IMUKB: IMU? = null
        val IMUSTOP: IMU? = null
        val IMUSLOW: IMU? = null
        val IMUWAVE: IMU? = null
        val IMUWEAK: IMU? = null
        val IMUWARP: IMU? = null
        val IMUCURSE: IMU? = null
        val SNIPER: PROB? = null
        val TIME: PT? = null
        val SEAL: PT? = null
        val SUMMON: SUMMON? = null
        val MOVEWAVE: MOVEWAVE? = null
        val THEME: THEME? = null
        val POISON: POISON? = null
        val BOSS: PROB? = null
        val CRITI: CRITI? = null
        val SATK: PM? = null
        val IMUATK: PT? = null
        val POIATK: PM? = null
        val VOLC: VOLC? = null
        val IMUPOIATK: IMU? = null
        val IMUVOLC: IMU? = null
        val ARMOR: ARMOR? = null
        val SPEED: SPEED? = null
        override fun clone(): Proc? {
            return try {
                val ans = Proc()
                val fs = this.javaClass.declaredFields
                for (f in fs) {
                    f.isAccessible = true
                    f[ans] = (f[this] as ProcItem).clone()
                    f.isAccessible = false
                }
                ans
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }

        operator fun get(id: String?): ProcItem? {
            return try {
                Proc::class.java.getField(id)[this] as ProcItem
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }

        fun getArr(i: Int): ProcItem? {
            return try {
                Proc::class.java.declaredFields[i][this] as ProcItem
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }

        override fun toString(): String {
            return JsonEncoder.Companion.encode(this).toString()
        }

        companion object {
            fun blank(): Proc {
                val ans = Proc()
                try {
                    val fs = Proc::class.java.declaredFields
                    for (i in fs.indices) {
                        fs[i].isAccessible = true
                        fs[i][ans] = (fs[i].type.newInstance() as ProcItem).clear()
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                return ans
            }

            fun getName(i: Int): String {
                return Proc::class.java.declaredFields[i].name
            }

            fun load(data: Array<IntArray>): Proc {
                val ans = Proc()
                try {
                    val fs = Proc::class.java.declaredFields
                    for (i in 0 until Math.min(data.size, fs.size)) {
                        fs[i].isAccessible = true
                        fs[i][ans] = (fs[i].type.newInstance() as ProcItem).load(data[i])
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                return ans
            }
        }
    }

    companion object {
        const val restrict_name = 32
        const val SE_HIT_0 = 20
        const val SE_HIT_1 = 21
        const val SE_DEATH_0 = 23
        const val SE_DEATH_1 = 24
        const val SE_HIT_BASE = 22
        const val SE_ZKILL = 59
        const val SE_CRIT = 44
        const val SE_SATK = 90
        const val SE_WAVE = 26
        const val SE_LETHAL = 50
        const val SE_WARP_ENTER = 73
        const val SE_WARP_EXIT = 74
        const val SE_BOSS = 45
        const val SE_SPEND_FAIL = 15 // TODO
        const val SE_SPEND_SUC = 19 // TODO
        const val SE_SPEND_REF = 27 // TODO
        const val SE_BARRIER_ABI = 70
        const val SE_BARRIER_NON = 71
        const val SE_BARRIER_ATK = 72
        const val SE_POISON = 110
        const val SE_VOLC_START = 111
        const val SE_VOLC_LOOP = 112
        val SE_CANNON = arrayOf(intArrayOf(25, 26), intArrayOf(60), intArrayOf(61), intArrayOf(36, 37), intArrayOf(65, 83), intArrayOf(84, 85), intArrayOf(86), intArrayOf(124))
        val SE_ALL = intArrayOf(15, 19, 20, 21, 22, 23, 24, 25, 26, 27, 36, 37, 44, 45, 50, 59, 60, 61, 65, 73,
                74, 83, 84, 85, 86, 90, 110, 111, 112, 124)
        const val RARITY_TOT = 6

        // trait bit filter
        const val TB_WHITE = 1
        const val TB_RED = 2
        const val TB_FLOAT = 4
        const val TB_BLACK = 8
        const val TB_METAL = 16
        const val TB_ANGEL = 32
        const val TB_ALIEN = 64
        const val TB_ZOMBIE = 128
        const val TB_RELIC = 256
        const val TB_EVA = 512
        const val TB_WITCH = 1024
        const val TB_INFH = 2048

        // trait index
        const val TRAIT_WHITE = 0
        const val TRAIT_RED = 1
        const val TRAIT_FLOAT = 2
        const val TRAIT_BLACK = 3
        const val TRAIT_METAL = 4
        const val TRAIT_ANGEL = 5
        const val TRAIT_ALIEN = 6
        const val TRAIT_ZOMBIE = 7
        const val TRAIT_RELIC = 8
        const val TRAIT_EVA = 9
        const val TRAIT_WITCH = 10
        const val TRAIT_INFH = 11
        const val TRAIT_TOT = 12

        // treasure
        const val T_RED = 0
        const val T_FLOAT = 1
        const val T_BLACK = 2
        const val T_ANGEL = 3
        const val T_METAL = 4
        const val T_ALIEN = 5
        const val T_ZOMBIE = 6

        // default tech value
        val MLV = intArrayOf(30, 30, 30, 30, 30, 30, 30, 10, 30)

        // tech index
        const val LV_RES = 0
        const val LV_ACC = 1
        const val LV_BASE = 2
        const val LV_WORK = 3
        const val LV_WALT = 4
        const val LV_RECH = 5
        const val LV_CATK = 6
        const val LV_CRG = 7
        const val LV_XP = 8
        const val LV_TOT = 9

        // default treasure value
        val MT = intArrayOf(300, 300, 300, 300, 300, 300, 600, 600, 600, 300, 300)

        // treasure index
        const val T_ATK = 0
        const val T_DEF = 1
        const val T_RES = 2
        const val T_ACC = 3
        const val T_WORK = 4
        const val T_WALT = 5
        const val T_RECH = 6
        const val T_CATK = 7
        const val T_BASE = 8
        const val T_XP1 = 9
        const val T_XP2 = 10
        const val T_TOT = 11

        // abi bit filter
        const val AB_GOOD = 1 shl 0
        const val AB_RESIST = 1 shl 1
        const val AB_MASSIVE = 1 shl 2
        const val AB_ONLY = 1 shl 3
        const val AB_EARN = 1 shl 4
        const val AB_BASE = 1 shl 5
        const val AB_METALIC = 1 shl 6
        const val AB_MOVEI = 1 shl 7
        const val AB_WAVES = 1 shl 8
        const val AB_SNIPERI = 1 shl 9
        const val AB_TIMEI = 1 shl 10
        const val AB_GHOST = 1 shl 11
        const val AB_POII = 1 shl 12
        const val AB_ZKILL = 1 shl 13
        const val AB_WKILL = 1 shl 14
        const val AB_GLASS = 1 shl 15
        const val AB_THEMEI = 1 shl 16
        const val AB_EKILL = 1 shl 17
        const val AB_SEALI = 1 shl 18
        const val AB_IMUSW = 1 shl 19
        const val AB_RESISTS = 1 shl 20
        const val AB_MASSIVES = 1 shl 21

        // 0111 1010 1110 0001 0111 1111
        @Deprecated("")
        val AB_ELIMINATOR = 0x7ae17f

        // abi index
        const val ABI_GOOD = 0
        const val ABI_RESIST = 1
        const val ABI_MASSIVE = 2
        const val ABI_ONLY = 3
        const val ABI_EARN = 4
        const val ABI_BASE = 5
        const val ABI_METALIC = 6
        const val ABI_MOVEI = 7
        const val ABI_WAVES = 8
        const val ABI_SNIPERI = 9
        const val ABI_TIMEI = 10
        const val ABI_GHOST = 11
        const val ABI_POII = 12
        const val ABI_ZKILL = 13
        const val ABI_WKILL = 14
        const val ABI_GLASS = 15
        const val ABI_THEMEI = 16
        const val ABI_EKILL = 17
        const val ABI_SEALI = 18
        const val ABI_IMUSW = 19
        const val ABI_RESISTS = 20
        const val ABI_MASSIVES = 21
        const val ABI_TOT = 30 // 20 currently

        // proc index
        const val P_KB = 0
        const val P_STOP = 1
        const val P_SLOW = 2
        const val P_CRIT = 3
        const val P_WAVE = 4
        const val P_WEAK = 5
        const val P_BREAK = 6
        const val P_WARP = 7
        const val P_CURSE = 8
        const val P_STRONG = 9
        const val P_LETHAL = 10
        const val P_BURROW = 11

        /**
         * body proc: 0: add revive time for zombies, -1 to make it infinite, revivable
         * zombies only 1: revive time 2: revive health 3: point 1 4: point 2 5: type:
         * 0/1/2/3: duration: in range and normal/in range/ master lifetime/permanent
         * +4: make Z-kill unusable +8: revive non-zombie also +16: applicapable to
         * others
         */
        const val P_REVIVE = 12
        const val P_IMUKB = 13
        const val P_IMUSTOP = 14
        const val P_IMUSLOW = 15
        const val P_IMUWAVE = 16
        const val P_IMUWEAK = 17
        const val P_IMUWARP = 18
        const val P_IMUCURSE = 19
        const val P_SNIPER = 20
        const val P_TIME = 21
        const val P_SEAL = 22
        /** 0:prob, 1:ID, 2:location, 3: buff, 4:conf, 5:time  */
        /**
         * +0: direct, +1: warp, +2:burrow, +4:disregard limit, +8: fix buff, +16: same
         * health, +32: diff layer, +64 on hit, +128 on kill
         */
        const val P_SUMMON = 23

        /**
         * 0:prob, 1:speed, 2:width (left to right), 3:time, 4:origin (center), 5:itv
         */
        const val P_MOVEWAVE = 24

        /**
         * 0:prob, 1:time (-1 means infinite), 2:ID, 3: type 0 : Change only BG 1 : Kill
         * all and change BG
         */
        const val P_THEME = 25

        /**
         * 0:prob, 1:time, 2:dmg, 3:itv, 4: conf +0: normal, +1: of total, +2: of
         * current, +3: of lost, +4: unstackable
         */
        const val P_POISON = 26
        const val P_BOSS = 27

        /**
         * body proc: 1: type: protect itself only (0) or effect the attack also (1)
         */
        const val P_CRITI = 28
        const val P_SATK = 29
        const val P_IMUATK = 30

        /** official poison  */
        const val P_POIATK = 31
        const val P_VOLC = 32
        const val P_IMUPOIATK = 33
        const val P_IMUVOLC = 34

        /**
         * Make target receive n% damage more/less 0: chance, 1: duration, 2: debuff
         */
        const val P_ARMOR = 35

        /**
         * Make target move faster/slower 0: chance, 1: duration, 2: speed, 3: type type
         * 0: Current speed * (100 + n)% type 1: Current speed + n type 2: Fixed speed
         */
        const val P_SPEED = 36
        const val PROC_TOT = 40 // 37
        const val PROC_WIDTH = 6
        const val WT_WAVE = 1
        const val WT_MOVE = 2
        const val WT_CANN = 2
        const val WT_VOLC = 4
        const val PC_P = 0
        const val PC_AB = 1
        const val PC_BASE = 2
        const val PC_IMU = 3
        const val PC_TRAIT = 4
        const val PC2_HP = 0
        const val PC2_ATK = 1
        const val PC2_SPEED = 2
        const val PC2_COST = 3
        const val PC2_CD = 4
        val PC_CORRES = arrayOf(intArrayOf(-1, 0), intArrayOf(0, Data.Companion.P_WEAK), intArrayOf(0, Data.Companion.P_STOP), intArrayOf(0, Data.Companion.P_SLOW), intArrayOf(1, Data.Companion.AB_ONLY, 0), intArrayOf(1, Data.Companion.AB_GOOD, 0), intArrayOf(1, Data.Companion.AB_RESIST, 0), intArrayOf(1, Data.Companion.AB_MASSIVE, 0), intArrayOf(0, Data.Companion.P_KB), intArrayOf(0, Data.Companion.P_WARP, 0), intArrayOf(0, Data.Companion.P_STRONG), intArrayOf(0, Data.Companion.P_LETHAL), intArrayOf(1, Data.Companion.AB_BASE, 0), intArrayOf(0, Data.Companion.P_CRIT), intArrayOf(1, Data.Companion.AB_ZKILL), intArrayOf(0, Data.Companion.P_BREAK), intArrayOf(1, Data.Companion.AB_EARN), intArrayOf(0, Data.Companion.P_WAVE), intArrayOf(0, Data.Companion.P_IMUWEAK), intArrayOf(0, Data.Companion.P_IMUSTOP), intArrayOf(0, Data.Companion.P_IMUSLOW), intArrayOf(0, Data.Companion.P_IMUKB), intArrayOf(0, Data.Companion.P_IMUWAVE), intArrayOf(1, Data.Companion.AB_WAVES, 0), intArrayOf(0, Data.Companion.P_IMUWARP, 0), intArrayOf(2, Data.Companion.PC2_COST), intArrayOf(2, Data.Companion.PC2_CD), intArrayOf(2, Data.Companion.PC2_SPEED), intArrayOf(-1, 0), intArrayOf(3, Data.Companion.P_IMUCURSE), intArrayOf(0, Data.Companion.P_IMUCURSE), intArrayOf(2, Data.Companion.PC2_ATK), intArrayOf(2, Data.Companion.PC2_HP), intArrayOf(4, Data.Companion.TB_RED, 0), intArrayOf(4, Data.Companion.TB_FLOAT, 0), intArrayOf(4, Data.Companion.TB_BLACK, 0), intArrayOf(4, Data.Companion.TB_METAL, 0), intArrayOf(4, Data.Companion.TB_ANGEL), intArrayOf(4, Data.Companion.TB_ALIEN), intArrayOf(4, Data.Companion.TB_ZOMBIE), intArrayOf(4, Data.Companion.TB_RELIC), intArrayOf(4, Data.Companion.TB_WHITE, 0), intArrayOf(-1, 0), intArrayOf(-1, 0), intArrayOf(-1, 0), intArrayOf(-1, 0), intArrayOf(-1, 0), intArrayOf(-1, 0), intArrayOf(3, Data.Companion.P_IMUWAVE), intArrayOf(3, Data.Companion.P_IMUWARP), intArrayOf(-1, 0), intArrayOf(0, Data.Companion.P_IMUATK), intArrayOf(-1, 0))

        // foot icon index used in battle
        const val INV = -1
        const val INVWARP = -2
        const val STPWAVE = -3
        const val BREAK_ABI = -4
        const val BREAK_ATK = -5
        const val BREAK_NON = -6

        // Combo index
        const val C_ATK = 0
        const val C_DEF = 1
        const val C_SPE = 2
        const val C_GOOD = 14
        const val C_MASSIVE = 15
        const val C_RESIST = 16
        const val C_KB = 17
        const val C_SLOW = 18
        const val C_STOP = 19
        const val C_WEAK = 20
        const val C_STRONG = 21
        const val C_WKILL = 22
        const val C_EKILL = 23
        const val C_CRIT = 24
        const val C_C_INI = 3
        const val C_C_ATK = 6
        const val C_C_SPE = 7
        const val C_BASE = 10
        const val C_M_INI = 5
        const val C_M_LV = 4
        const val C_M_MAX = 9
        const val C_RESP = 11
        const val C_MEAR = 12
        const val C_XP = 13 // abandoned
        const val C_TOT = 25

        // Effects Anim index
        const val A_KB = 29
        const val A_CRIT = 28
        const val A_SHOCKWAVE = 27
        const val A_ZOMBIE = 26
        const val A_EFF_INV = 18
        const val A_EFF_DEF = 19 // unused
        const val A_Z_STRONG = 20
        const val A_B = 21
        const val A_E_B = 22
        const val A_W = 23
        const val A_W_C = 24
        const val A_CURSE = 25
        const val A_DOWN = 0
        const val A_UP = 2
        const val A_SLOW = 4
        const val A_STOP = 6
        const val A_SHIELD = 8
        const val A_FARATTACK = 10
        const val A_WAVE_INVALID = 12
        const val A_WAVE_STOP = 14
        const val A_WAVEGUARD = 16 // unused
        const val A_E_DOWN = 1
        const val A_E_UP = 3
        const val A_E_SLOW = 5
        const val A_E_STOP = 7
        const val A_E_SHIELD = 9
        const val A_E_FARATTACK = 11
        const val A_E_WAVE_INVALID = 13
        const val A_E_WAVE_STOP = 15
        const val A_E_WAVEGUARD = 17 // unused
        const val A_SNIPER = 30
        const val A_U_ZOMBIE = 31
        const val A_U_B = 32
        const val A_U_E_B = 33
        const val A_SEAL = 34
        const val A_POI0 = 35
        const val A_POI1 = 36
        const val A_POI2 = 37
        const val A_POI3 = 38
        const val A_POI4 = 39
        const val A_POI5 = 40
        const val A_POI6 = 41
        const val A_POI7 = 42
        const val A_SATK = 43
        const val A_IMUATK = 44
        const val A_POISON = 45
        const val A_VOLC = 46
        const val A_E_VOLC = 47
        const val A_E_CURSE = 48
        const val A_WAVE = 49
        const val A_E_WAVE = 50
        const val A_ARMOR = 51
        const val A_E_ARMOR = 52
        const val A_SPEED = 53
        const val A_E_SPEED = 54
        const val A_WEAK_UP = 55
        const val A_E_WEAK_UP = 56
        val A_POIS = intArrayOf(Data.Companion.A_POI0, Data.Companion.A_POI1, Data.Companion.A_POI2, Data.Companion.A_POI3, Data.Companion.A_POI4, Data.Companion.A_POI5, Data.Companion.A_POI6, Data.Companion.A_POI7)
        const val A_TOT = 57

        // atk type index used in filter page
        const val ATK_SINGLE = 0
        const val ATK_AREA = 1
        const val ATK_LD = 2
        const val ATK_OMNI = 4
        const val ATK_TOT = 6

        // base and canon level
        const val BASE_H = 0
        const val BASE_SLOW = 1
        const val BASE_WALL = 2
        const val BASE_STOP = 3
        const val BASE_WATER = 4
        const val BASE_GROUND = 5
        const val BASE_BARRIER = 6
        const val BASE_CURSE = 7
        const val BASE_TOT = 8

        // touchable ID
        const val TCH_N = 1
        const val TCH_KB = 2
        const val TCH_UG = 4
        const val TCH_CORPSE = 8
        const val TCH_SOUL = 16
        const val TCH_EX = 32
        const val TCH_ZOMBX = 64
        val A_PATH = arrayOf("down", "up", "slow", "stop", "shield", "farattack",
                "wave_invalid", "wave_stop", "waveguard")

        // After this line all number is game data
        const val INT_KB = 0
        const val INT_HB = 1
        const val INT_SW = 2
        const val INT_ASS = 3
        const val INT_WARP = 4
        val KB_PRI = intArrayOf(2, 4, 5, 1, 3)
        val KB_TIME = intArrayOf(11, 23, 47, 11, -1)
        val KB_DIS = intArrayOf(165, 345, 705, 55, -1)
        const val W_E_INI = -33
        const val W_U_INI = -67
        const val W_PROG = 200
        const val W_E_WID = 500
        const val W_U_WID = 400
        const val W_TIME = 3
        const val E_IMU = -1
        const val E_IWAVE = -2
        const val E_SWAVE = -3
        const val W_VOLC = 375
        const val W_VOLC_INNER = 250 // volcano inner width
        const val W_VOLC_PIERCE = 125 // volcano pierce width
        const val VOLC_ITV = 20
        const val VOLC_PRE = 15 // volcano pre-atk
        const val VOLC_POST = 10 // volcano post-atk
        const val VOLC_SE = 30 // volcano se loop duration
        val NYPRE = intArrayOf(18, 2, -1, 28, 37, 18, 10, 2) // not sure
        val NYRAN = intArrayOf(710, 600, -1, 500, 500, 710, 100, 600) // not sure
        const val SNIPER_CD = 300 // not sure
        const val SNIPER_PRE = 5 // not sure
        const val SNIPER_POS = -500 // not sure
        const val REVIVE_SHOW_TIME = 16
        const val ORB_ATK = 0
        const val ORB_RES = 1
        const val ORB_TYPE = 0
        const val ORB_TRAIT = 1
        const val ORB_GRADE = 2
        val ORB_ATK_MULTI = intArrayOf(100, 200, 300, 400, 500) // Atk orb multiplication
        val ORB_RES_MULTI = intArrayOf(4, 8, 12, 16, 20) // Resist orb multiplication
        val SUFX = arrayOf("f", "c", "s")
        fun effas(): EffAnimStore {
            return CommonStatic.getBCAssets().effas
        }

        /**
         * convenient method to log an unexpected error. Don't use it to process any
         * expected error
         */
        fun err(s: Context.RunExc?): Boolean {
            return CommonStatic.ctx.noticeErr(s, ErrType.ERROR, "unexpected error")
        }

        /**
         * convenient method to log an unexpected error. Don't use it to process any
         * expected error
         */
        fun <T> err(s: SupExc<T>?): T {
            return CommonStatic.ctx.noticeErr(s, ErrType.ERROR, "unexpected error")
        }

        fun getVer(ver: String): Int {
            var ans = 0
            val strs = ver.split("\\.").toTypedArray()
            for (str in strs) {
                ans *= 100
                ans += str.toInt()
            }
            return ans
        }

        fun hex(id: Int): String {
            return Data.Companion.trio(id / 1000) + Data.Companion.trio(id % 1000)
        }

        fun <T> ignore(sup: SupExc<T>): T? {
            return try {
                sup.get()
            } catch (e: Exception) {
                null
            }
        }

        fun restrict(str: String): String {
            return if (str.length < Data.Companion.restrict_name) str else str.substring(0, Data.Companion.restrict_name)
        }

        fun revVer(ver: Int): String {
            return (ver / 1000000 % 100).toString() + "-" + ver / 10000 % 100 + "-" + ver / 100 % 100 + "-" + ver % 100
        }

        fun trio(i: Int): String {
            var i = i
            i %= 1000
            var str = ""
            if (i < 100) str += "0"
            if (i < 10) str += "0"
            return str + i
        }
    }
}