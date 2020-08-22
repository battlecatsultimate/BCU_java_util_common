package common.util.animimport

import common.pack.UserProfile
import common.util.anim.Part
import java.util.*

com.google.api.client.json.jackson2.JacksonFactoryimport com.google.api.services.drive.DriveScopesimport com.google.api.client.util.store.FileDataStoreFactoryimport com.google.api.client.http.HttpTransportimport com.google.api.services.drive.Driveimport kotlin.Throwsimport java.io.IOExceptionimport io.drive.DriveUtilimport java.io.FileNotFoundExceptionimport java.io.FileInputStreamimport com.google.api.client.googleapis.auth.oauth2.GoogleClientSecretsimport java.io.InputStreamReaderimport com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlowimport com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledAppimport com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiverimport com.google.api.client.googleapis.javanet.GoogleNetHttpTransportimport kotlin.jvm.JvmStaticimport io.drive.DrvieInitimport com.google.api.client.http.javanet.NetHttpTransportimport com.google.api.services.drive.model.FileListimport java.io.BufferedInputStreamimport java.io.FileOutputStreamimport com.google.api.client.googleapis.media.MediaHttpDownloaderimport io.WebFileIOimport io.BCJSONimport page.LoadPageimport org.json.JSONObjectimport org.json.JSONArrayimport main.MainBCUimport main.Optsimport common.CommonStaticimport java.util.TreeMapimport java.util.Arraysimport java.io.BufferedReaderimport io.BCMusicimport common.util.stage.Musicimport io.BCPlayerimport java.util.HashMapimport javax.sound.sampled.Clipimport java.io.ByteArrayInputStreamimport javax.sound.sampled.AudioInputStreamimport javax.sound.sampled.AudioSystemimport javax.sound.sampled.DataLineimport javax.sound.sampled.FloatControlimport javax.sound.sampled.LineEventimport com.google.api.client.googleapis.media.MediaHttpDownloaderProgressListenerimport com.google.api.client.googleapis.media.MediaHttpDownloader.DownloadStateimport common.io.DataIOimport io.BCUReaderimport common.io.InStreamimport com.google.gson.JsonElementimport common.io.json.JsonDecoderimport com.google.gson.JsonObjectimport page.MainFrameimport page.view.ViewBox.Confimport page.MainLocaleimport page.battle.BattleInfoPageimport page.support.Exporterimport page.support.Importerimport common.pack.Context.ErrTypeimport common.util.stage.MapColcimport common.util.stage.MapColc.DefMapColcimport common.util.lang.MultiLangContimport common.util.stage.StageMapimport common.util.unit.Enemyimport io.BCUWriterimport java.text.SimpleDateFormatimport java.io.PrintStreamimport common.io.OutStreamimport common.battle.BasisSetimport res.AnimatedGifEncoderimport java.awt.image.BufferedImageimport javax.imageio.ImageIOimport java.security.MessageDigestimport java.security.NoSuchAlgorithmExceptionimport common.io.json.JsonEncoderimport java.io.FileWriterimport com.google.api.client.http.GenericUrlimport org.apache.http.impl .client.CloseableHttpClientimport org.apache.http.impl .client.HttpClientsimport org.apache.http.client.methods.HttpPostimport org.apache.http.entity.mime.content.FileBodyimport org.apache.http.entity.mime.MultipartEntityBuilderimport org.apache.http.entity.mime.HttpMultipartModeimport org.apache.http.HttpEntityimport org.apache.http.util.EntityUtilsimport com.google.api.client.http.HttpRequestInitializerimport com.google.api.client.http.HttpBackOffUnsuccessfulResponseHandlerimport com.google.api.client.util.ExponentialBackOffimport com.google.api.client.http.HttpBackOffIOExceptionHandlerimport res.NeuQuantimport res.LZWEncoderimport java.io.BufferedOutputStreamimport java.awt.Graphics2Dimport java.awt.image.DataBufferByteimport common.system.fake.FakeImageimport utilpc.awt.FIBIimport jogl.util.AmbImageimport common.system.files.VFileimport jogl.util.GLImageimport com.jogamp.opengl.util.texture.TextureDataimport common.system.Pimport com.jogamp.opengl.util.texture.TextureIOimport jogl.GLStaticimport com.jogamp.opengl.util.texture.awt.AWTTextureIOimport java.awt.AlphaCompositeimport common.system.fake.FakeImage.Markerimport jogl.util.GLGraphicsimport com.jogamp.opengl.GL2import jogl.util.GeoAutoimport com.jogamp.opengl.GL2ES3import com.jogamp.opengl.GLimport common.system.fake.FakeGraphicsimport common.system.fake.FakeTransformimport jogl.util.ResManagerimport jogl.util.GLGraphics.GeomGimport jogl.util.GLGraphics.GLCimport jogl.util.GLGraphics.GLTimport com.jogamp.opengl.GL2ES2import com.jogamp.opengl.util.glsl.ShaderCodeimport com.jogamp.opengl.util.glsl.ShaderProgramimport com.jogamp.opengl.GLExceptionimport jogl.StdGLCimport jogl.Tempimport common.util.anim.AnimUimport common.util.anim.EAnimUimport jogl.util.GLIBimport javax.swing.JFrameimport common.util.anim.AnimCEimport common.util.anim.AnimU.UTypeimport com.jogamp.opengl.util.FPSAnimatorimport com.jogamp.opengl.GLEventListenerimport com.jogamp.opengl.GLAutoDrawableimport page.awt.BBBuilderimport page.battle.BattleBox.OuterBoximport common.battle.SBCtrlimport page.battle.BattleBoximport jogl.GLBattleBoximport common.battle.BattleFieldimport page.anim.IconBoximport jogl.GLIconBoximport jogl.GLBBRecdimport page.awt.RecdThreadimport page.view.ViewBoximport jogl.GLViewBoximport page.view.ViewBox.Controllerimport java.awt.AWTExceptionimport page.battle.BBRecdimport jogl.GLRecorderimport com.jogamp.opengl.GLProfileimport com.jogamp.opengl.GLCapabilitiesimport page.anim.IconBox.IBCtrlimport page.anim.IconBox.IBConfimport page.view.ViewBox.VBExporterimport jogl.GLRecdBImgimport page.JTGimport jogl.GLCstdimport jogl.GLVBExporterimport common.util.anim.EAnimIimport page.RetFuncimport page.battle.BattleBox.BBPainterimport page.battle.BBCtrlimport javax.swing.JOptionPaneimport kotlin.jvm.Strictfpimport main.Invimport javax.swing.SwingUtilitiesimport java.lang.InterruptedExceptionimport utilpc.UtilPC.PCItrimport utilpc.awt.PCIBimport jogl.GLBBBimport page.awt.AWTBBBimport utilpc.Themeimport page.MainPageimport common.io.assets.AssetLoaderimport common.pack.Source.Workspaceimport common.io.PackLoader.ZipDesc.FileDescimport common.io.assets.Adminimport page.awt.BattleBoxDefimport page.awt.IconBoxDefimport page.awt.BBRecdAWTimport page.awt.ViewBoxDefimport org.jcodec.api.awt.AWTSequenceEncoderimport page.awt.RecdThread.PNGThreadimport page.awt.RecdThread.MP4Threadimport page.awt.RecdThread.GIFThreadimport java.awt.GradientPaintimport utilpc.awt.FG2Dimport page.anim.TreeContimport javax.swing.JTreeimport javax.swing.event.TreeExpansionListenerimport common.util.anim.MaModelimport javax.swing.tree.DefaultMutableTreeNodeimport javax.swing.event.TreeExpansionEventimport java.util.function.IntPredicateimport javax.swing.tree.DefaultTreeModelimport common.util.anim.EAnimDimport page.anim.AnimBoximport utilpc.PPimport common.CommonStatic.BCAuxAssetsimport common.CommonStatic.EditLinkimport page.JBTNimport page.anim.DIYViewPageimport page.anim.ImgCutEditPageimport page.anim.MaModelEditPageimport page.anim.MaAnimEditPageimport page.anim.EditHeadimport java.awt.event.ActionListenerimport page.anim.AbEditPageimport common.util.anim.EAnimSimport page.anim.ModelBoximport common.util.anim.ImgCutimport page.view.AbViewPageimport javax.swing.JListimport javax.swing.JScrollPaneimport javax.swing.JComboBoximport utilpc.UtilPCimport javax.swing.event.ListSelectionListenerimport javax.swing.event.ListSelectionEventimport common.system.VImgimport page.support.AnimLCRimport page.support.AnimTableimport common.util.anim.MaAnimimport java.util.EventObjectimport javax.swing.text.JTextComponentimport page.anim.PartEditTableimport javax.swing.ListSelectionModelimport page.support.AnimTableTHimport page.JTFimport utilpc.ReColorimport page.anim.ImgCutEditTableimport page.anim.SpriteBoximport page.anim.SpriteEditPageimport java.awt.event.FocusAdapterimport java.awt.event.FocusEventimport common.pack.PackData.UserPackimport utilpc.Algorithm.SRResultimport page.anim.MaAnimEditTableimport javax.swing.JSliderimport java.awt.event.MouseWheelEventimport common.util.anim.EPartimport javax.swing.event.ChangeEventimport page.anim.AdvAnimEditPageimport javax.swing.BorderFactoryimport page.JLimport javax.swing.ImageIconimport page.anim.MMTreeimport javax.swing.event.TreeSelectionListenerimport javax.swing.event.TreeSelectionEventimport page.support.AbJTableimport page.anim.MaModelEditTableimport page.info.edit.ProcTableimport page.info.edit.ProcTable.AtkProcTableimport page.info.edit.SwingEditorimport page.info.edit.ProcTable.MainProcTableimport page.support.ListJtfPolicyimport page.info.edit.SwingEditor.SwingEGimport common.util.Data.Procimport java.lang.Runnableimport javax.swing.JComponentimport page.info.edit.LimitTableimport page.pack.CharaGroupPageimport page.pack.LvRestrictPageimport javax.swing.SwingConstantsimport common.util.lang.Editors.EditorGroupimport common.util.lang.Editors.EdiFieldimport common.util.lang.Editorsimport common.util.lang.ProcLangimport page.info.edit.EntityEditPageimport common.util.lang.Editors.EditorSupplierimport common.util.lang.Editors.EditControlimport page.info.edit.SwingEditor.IntEditorimport page.info.edit.SwingEditor.BoolEditorimport page.info.edit.SwingEditor.IdEditorimport page.SupPageimport common.util.unit.AbEnemyimport common.pack.IndexContainer.Indexableimport common.pack.Context.SupExcimport common.battle.data .AtkDataModelimport utilpc.Interpretimport common.battle.data .CustomEntityimport page.info.filter.UnitEditBoximport common.battle.data .CustomUnitimport common.util.stage.SCGroupimport page.info.edit.SCGroupEditTableimport common.util.stage.SCDefimport page.info.filter.EnemyEditBoximport common.battle.data .CustomEnemyimport page.info.StageFilterPageimport page.view.BGViewPageimport page.view.CastleViewPageimport page.view.MusicPageimport common.util.stage.CastleImgimport common.util.stage.CastleListimport java.text.DecimalFormatimport common.util.stage.Recdimport common.util.stage.MapColc.PackMapColcimport page.info.edit.StageEditTableimport page.support.ReorderListimport page.info.edit.HeadEditTableimport page.info.filter.EnemyFindPageimport page.battle.BattleSetupPageimport page.info.edit.AdvStEditPageimport page.battle.StRecdPageimport page.info.edit.LimitEditPageimport page.support.ReorderListenerimport common.util.pack.Soulimport page.info.edit.AtkEditTableimport page.info.filter.UnitFindPageimport common.battle.Basisimport common.util.Data.Proc.IMUimport javax.swing.DefaultComboBoxModelimport common.util.Animableimport common.util.pack.Soul.SoulTypeimport page.view.UnitViewPageimport page.view.EnemyViewPageimport page.info.edit.SwingEditor.EditCtrlimport page.support.Reorderableimport page.info.EnemyInfoPageimport common.util.unit.EneRandimport page.pack.EREditPageimport page.support.InTableTHimport page.support.EnemyTCRimport javax.swing.DefaultListCellRendererimport page.info.filter.UnitListTableimport page.info.filter.UnitFilterBoximport page.info.filter.EnemyListTableimport page.info.filter.EnemyFilterBoximport page.info.filter.UFBButtonimport page.info.filter.UFBListimport common.battle.data .MaskUnitimport javax.swing.AbstractButtonimport page.support.SortTableimport page.info.UnitInfoPageimport page.support.UnitTCRimport page.info.filter.EFBButtonimport page.info.filter.EFBListimport common.util.stage.LvRestrictimport common.util.stage.CharaGroupimport page.info.StageTableimport page.info.TreaTableimport javax.swing.JPanelimport page.info.UnitInfoTableimport page.basis.BasisPageimport kotlin.jvm.JvmOverloadsimport page.info.EnemyInfoTableimport common.util.stage.RandStageimport page.info.StagePageimport page.info.StageRandPageimport common.util.unit.EFormimport page.pack.EREditTableimport common.util.EREntimport common.pack.FixIndexListimport page.support.UnitLCRimport page.pack.RecdPackPageimport page.pack.CastleEditPageimport page.pack.BGEditPageimport page.pack.CGLREditPageimport common.pack.Source.ZipSourceimport page.info.edit.EnemyEditPageimport page.info.edit.StageEditPageimport page.info.StageViewPageimport page.pack.UnitManagePageimport page.pack.MusicEditPageimport page.battle.AbRecdPageimport common.system.files.VFileRootimport java.awt.Desktopimport common.pack.PackDataimport common.util.unit.UnitLevelimport page.info.edit.FormEditPageimport common.util.anim.AnimIimport common.util.anim.AnimI.AnimTypeimport common.util.anim.AnimDimport common.battle.data .Orbimport page.basis.LineUpBoximport page.basis.LubContimport common.battle.BasisLUimport page.basis.ComboListTableimport page.basis.ComboListimport page.basis.NyCasBoximport page.basis.UnitFLUPageimport common.util.unit.Comboimport page.basis.LevelEditPageimport common.util.pack.NyCastleimport common.battle.LineUpimport common.system.SymCoordimport java.util.TreeSetimport page.basis.OrbBoximport javax.swing.table.DefaultTableCellRendererimport javax.swing.JTableimport common.CommonStatic.BattleConstimport common.battle.StageBasisimport common.util.ImgCoreimport common.battle.attack.ContAbimport common.battle.entity.EAnimContimport common.battle.entity.WaprContimport page.battle.RecdManagePageimport page.battle.ComingTableimport common.util.stage.EStageimport page.battle.EntityTableimport common.battle.data .MaskEnemyimport common.battle.SBRplyimport common.battle.entity.AbEntityimport page.battle.RecdSavePageimport page.LocCompimport page.LocSubCompimport javax.swing.table.TableModelimport page.support.TModelimport javax.swing.event.TableModelListenerimport javax.swing.table.DefaultTableColumnModelimport javax.swing.JFileChooserimport javax.swing.filechooser.FileNameExtensionFilterimport javax.swing.TransferHandlerimport java.awt.datatransfer.Transferableimport java.awt.datatransfer.DataFlavorimport javax.swing.DropModeimport javax.swing.TransferHandler.TransferSupportimport java.awt.dnd.DragSourceimport java.awt.datatransfer.UnsupportedFlavorExceptionimport common.system.Copableimport page.support.AnimTransferimport javax.swing.DefaultListModelimport page.support.InListTHimport java.awt.FocusTraversalPolicyimport javax.swing.JTextFieldimport page.CustomCompimport javax.swing.JToggleButtonimport javax.swing.JButtonimport javax.swing.ToolTipManagerimport javax.swing.JRootPaneimport javax.swing.JProgressBarimport page.ConfigPageimport page.view.EffectViewPageimport page.pack.PackEditPageimport page.pack.ResourcePageimport javax.swing.WindowConstantsimport java.awt.event.AWTEventListenerimport java.awt.AWTEventimport java.awt.event.ComponentAdapterimport java.awt.event.ComponentEventimport java.util.ConcurrentModificationExceptionimport javax.swing.plaf.FontUIResourceimport java.util.Enumerationimport javax.swing.UIManagerimport common.CommonStatic.FakeKeyimport page.LocSubComp.LocBinderimport page.LSCPopimport java.awt.BorderLayoutimport java.awt.GridLayoutimport javax.swing.JTextPaneimport page.TTTimport java.util.ResourceBundleimport java.util.MissingResourceExceptionimport java.util.Localeimport common.io.json.Test.JsonTest_2import common.pack.PackData.PackDescimport common.io.PackLoaderimport common.io.PackLoader.Preloadimport common.io.PackLoader.ZipDescimport common.io.json.Testimport common.io.json.JsonClassimport common.io.json.JsonFieldimport common.io.json.JsonField.GenTypeimport common.io.json.Test.JsonTest_0.JsonDimport common.io.json.JsonClass.RTypeimport java.util.HashSetimport common.io.json.JsonDecoder.OnInjectedimport common.io.json.JsonField.IOTypeimport common.io.json.JsonExceptionimport common.io.json.JsonClass.NoTagimport common.io.json.JsonField.SerTypeimport common.io.json.JsonClass.WTypeimport kotlin.reflect.KClassimport com.google.gson.JsonArrayimport common.io.assets.Admin.StaticPermittedimport common.io.json.JsonClass.JCGenericimport common.io.json.JsonClass.JCGetterimport com.google.gson.JsonPrimitiveimport com.google.gson.JsonNullimport common.io.json.JsonClass.JCIdentifierimport java.lang.ClassNotFoundExceptionimport common.io.assets.AssetLoader.AssetHeaderimport common.io.assets.AssetLoader.AssetHeader.AssetEntryimport common.io.InStreamDefimport common.io.BCUExceptionimport java.io.UnsupportedEncodingExceptionimport common.io.OutStreamDefimport javax.crypto.Cipherimport javax.crypto.spec.IvParameterSpecimport javax.crypto.spec.SecretKeySpecimport common.io.PackLoader.FileSaverimport common.system.files.FDByteimport common.io.json.JsonClass.JCConstructorimport common.io.PackLoader.FileLoader.FLStreamimport common.io.PackLoader.PatchFileimport java.lang.NullPointerExceptionimport java.lang.IndexOutOfBoundsExceptionimport common.io.MultiStreamimport java.io.RandomAccessFileimport common.io.MultiStream.TrueStreamimport java.lang.RuntimeExceptionimport common.pack.Source.ResourceLocationimport common.pack.Source.AnimLoaderimport common.pack.Source.SourceAnimLoaderimport common.util.anim.AnimCIimport common.system.files.FDFileimport common.pack.IndexContainerimport common.battle.data .PCoinimport common.util.pack.EffAnimimport common.battle.data .DataEnemyimport common.util.stage.Limit.DefLimitimport common.pack.IndexContainer.Reductorimport common.pack.FixIndexList.FixIndexMapimport common.pack.VerFixer.IdFixerimport common.pack.IndexContainer.IndexContimport common.pack.IndexContainer.ContGetterimport common.util.stage.CastleList.PackCasListimport common.util.Data.Proc.THEMEimport common.CommonStatic.ImgReaderimport common.pack.VerFixerimport common.pack.VerFixer.VerFixerExceptionimport java.lang.NumberFormatExceptionimport common.pack.Source.SourceAnimSaverimport common.pack.VerFixer.EnemyFixerimport common.pack.VerFixer.PackFixerimport common.pack.PackData.DefPackimport java.util.function.BiConsumerimport common.util.BattleStaticimport common.util.anim.AnimU.ImageKeeperimport common.util.anim.AnimCE.AnimCELoaderimport common.util.anim.AnimCI.AnimCIKeeperimport common.util.anim.AnimUD.DefImgLoaderimport common.util.BattleObjimport common.util.Data.Proc.ProcItemimport common.util.lang.ProcLang.ItemLangimport common.util.lang.LocaleCenter.Displayableimport common.util.lang.Editors.DispItemimport common.util.lang.LocaleCenter.ObjBinderimport common.util.lang.LocaleCenter.ObjBinder.BinderFuncimport common.util.Data.Proc.PROBimport org.jcodec.common.tools.MathUtilimport common.util.Data.Proc.PTimport common.util.Data.Proc.PTDimport common.util.Data.Proc.PMimport common.util.Data.Proc.WAVEimport common.util.Data.Proc.WEAKimport common.util.Data.Proc.STRONGimport common.util.Data.Proc.BURROWimport common.util.Data.Proc.REVIVEimport common.util.Data.Proc.SUMMONimport common.util.Data.Proc.MOVEWAVEimport common.util.Data.Proc.POISONimport common.util.Data.Proc.CRITIimport common.util.Data.Proc.VOLCimport common.util.Data.Proc.ARMORimport common.util.Data.Proc.SPEEDimport java.util.LinkedHashMapimport common.util.lang.LocaleCenter.DisplayItemimport common.util.lang.ProcLang.ProcLangStoreimport common.util.lang.Formatter.IntExpimport common.util.lang.Formatter.RefObjimport common.util.lang.Formatter.BoolExpimport common.util.lang.Formatter.BoolElemimport common.util.lang.Formatter.IElemimport common.util.lang.Formatter.Contimport common.util.lang.Formatter.Elemimport common.util.lang.Formatter.RefElemimport common.util.lang.Formatter.RefFieldimport common.util.lang.Formatter.RefFuncimport common.util.lang.Formatter.TextRefimport common.util.lang.Formatter.CodeBlockimport common.util.lang.Formatter.TextPlainimport common.util.unit.Unit.UnitInfoimport common.util.lang.MultiLangCont.MultiLangStaticsimport common.util.pack.EffAnim.EffTypeimport common.util.pack.EffAnim.ArmorEffimport common.util.pack.EffAnim.BarEneEffimport common.util.pack.EffAnim.BarrierEffimport common.util.pack.EffAnim.DefEffimport common.util.pack.EffAnim.WarpEffimport common.util.pack.EffAnim.ZombieEffimport common.util.pack.EffAnim.KBEffimport common.util.pack.EffAnim.SniperEffimport common.util.pack.EffAnim.VolcEffimport common.util.pack.EffAnim.SpeedEffimport common.util.pack.EffAnim.WeakUpEffimport common.util.pack.EffAnim.EffAnimStoreimport common.util.pack.NyCastle.NyTypeimport common.util.pack.WaveAnimimport common.util.pack.WaveAnim.WaveTypeimport common.util.pack.Background.BGWvTypeimport common.util.unit.Form.FormJsonimport common.system.BasedCopableimport common.util.anim.AnimUDimport common.battle.data .DataUnitimport common.battle.entity.EUnitimport common.battle.entity.EEnemyimport common.util.EntRandimport common.util.stage.Recd.Waitimport java.lang.CloneNotSupportedExceptionimport common.util.stage.StageMap.StageMapInfoimport common.util.stage.Stage.StageInfoimport common.util.stage.Limit.PackLimitimport common.util.stage.MapColc.ClipMapColcimport common.util.stage.CastleList.DefCasListimport common.util.stage.MapColc.StItrimport common.util.Data.Proc.IntType.BitCountimport common.util.CopRandimport common.util.LockGLimport java.lang.IllegalAccessExceptionimport common.battle.data .MaskAtkimport common.battle.data .DefaultDataimport common.battle.data .DataAtkimport common.battle.data .MaskEntityimport common.battle.data .DataEntityimport common.battle.attack.AtkModelAbimport common.battle.attack.AttackAbimport common.battle.attack.AttackSimpleimport common.battle.attack.AttackWaveimport common.battle.entity.Cannonimport common.battle.attack.AttackVolcanoimport common.battle.attack.ContWaveAbimport common.battle.attack.ContWaveDefimport common.battle.attack.AtkModelEntityimport common.battle.entity.EntContimport common.battle.attack.ContMoveimport common.battle.attack.ContVolcanoimport common.battle.attack.ContWaveCanonimport common.battle.attack.AtkModelEnemyimport common.battle.attack.AtkModelUnitimport common.battle.attack.AttackCanonimport common.battle.entity.EUnit.OrbHandlerimport common.battle.entity.Entity.AnimManagerimport common.battle.entity.Entity.AtkManagerimport common.battle.entity.Entity.ZombXimport common.battle.entity.Entity.KBManagerimport common.battle.entity.Entity.PoisonTokenimport common.battle.entity.Entity.WeakTokenimport common.battle.Treasureimport common.battle.MirrorSetimport common.battle.Releaseimport common.battle.ELineUpimport common.battle.entity.Sniperimport common.battle.entity.ECastleimport java.util.Dequeimport common.CommonStatic.Itfimport java.lang.Characterimport common.CommonStatic.ImgWriterimport utilpc.awt.FTATimport utilpc.awt.Blenderimport java.awt.RenderingHintsimport utilpc.awt.BIBuilderimport java.awt.CompositeContextimport java.awt.image.Rasterimport java.awt.image.WritableRasterimport utilpc.ColorSetimport utilpc.OggTimeReaderimport utilpc.UtilPC.PCItr.MusicReaderimport utilpc.UtilPC.PCItr.PCALimport javax.swing.UIManager.LookAndFeelInfoimport java.lang.InstantiationExceptionimport javax.swing.UnsupportedLookAndFeelExceptionimport utilpc.Algorithm.ColorShiftimport utilpc.Algorithm.StackRect
class AnimCE : AnimCI {
    private class AnimCELoader(private val name: String) : AnimLoader {
        private val pre: String
        override fun getEdi(): VImg? {
            return optional(pre + "edi.png")
        }

        override fun getIC(): ImgCut {
            return ImgCut.Companion.newIns("$pre$name.imgcut")
        }

        override fun getMA(): Array<MaAnim?> {
            val anims: Array<MaAnim?> = arrayOfNulls<MaAnim>(7)
            for (i in 0..3) anims[i] = MaAnim.Companion.newIns(pre + name + "0" + i + ".maanim")
            for (i in 0..2) anims[i + 4] = MaAnim.Companion.newIns(pre + name + "_zombie0" + i + ".maanim")
            return anims
        }

        override fun getMM(): MaModel {
            return MaModel.Companion.newIns("$pre$name.mamodel")
        }

        override fun getName(): ResourceLocation {
            return ResourceLocation("_local", name)
        }

        override fun getNum(): FakeImage? {
            return VFile.Companion.getFile("$pre$name.png").getData().getImg()
        }

        override fun getStatus(): Int {
            return 0
        }

        override fun getUni(): VImg? {
            return optional(pre + "uni.png")
        }

        companion object {
            private fun optional(str: String): VImg? {
                val fv: VFile<*> = VFile.Companion.getFile(str) ?: return null
                return VImg(fv)
            }
        }

        init {
            pre = "./res/anim/$name/"
        }
    }

    private inner class History(val name: String, os: OutStream) {
        val data: OutStream
        var mms: OutStream? = null

        init {
            data = os
        }
    }

    private var saved = false
    var link: EditLink? = null
    var history = Stack<common.util.anim.AnimCE.History>()

    constructor(resourceLocation: ResourceLocation) : super(SourceAnimLoader(resourceLocation, null)) {
        id = resourceLocation
        map()[id.id] = this
        history("initial")
    }

    /** for conversion only  */
    @Deprecated("")
    constructor(al: AnimLoader) : super(al) {
    }

    constructor(st: String) : super(AnimCELoader(st)) {
        id = ResourceLocation("_local", st)
        map()[id.id] = this
    }

    constructor(str: String?, ori: AnimD<*, *>) : this(str) {
        loaded = true
        partial = true
        imgcut = ori.imgcut!!.clone()
        mamodel = ori.mamodel!!.clone()
        if (mamodel.confs.size < 1) mamodel.confs = Array<IntArray?>(2) { IntArray(6) }
        anims = arrayOfNulls<MaAnim>(7)
        for (i in 0..6) if (i < ori.anims.size) anims.get(i) = ori.anims.get(i).clone() else anims.get(i) = MaAnim()
        loader.setNum(ori.getNum())
        parts = imgcut.cut(ori.getNum())
        if (ori is AnimU<*>) {
            val au: AnimU<*> = ori
            setEdi(au.getEdi())
            setUni(au.getUni())
        }
        standardize()
        save()
        history("initial")
    }

    fun createNew() {
        loaded = true
        partial = true
        imgcut = ImgCut()
        mamodel = MaModel()
        anims = arrayOfNulls<MaAnim>(7)
        for (i in 0..6) anims.get(i) = MaAnim()
        parts = imgcut.cut(getNum())
        history("initial")
    }

    fun deletable(): Boolean {
        for (p in UserProfile.Companion.getUserPacks()) {
            for (e in p.enemies.getList()) if (e.anim === this) return false
            for (u in p.units.getList()) for (f in u.forms) if (f.anim === this) return false
        }
        return true
    }

    fun delete() {
        map().remove(id.id)
        SourceAnimSaver(id, this).delete()
    }

    fun getUndo(): String {
        return history.peek().name
    }

    fun hardSave(str: String?) {
        if (id == null) {
            id = Workspace.Companion.validate(ResourceLocation("_local", str))
            map()[id.id] = this
        }
        saved = false
        save()
    }

    fun ICedited() {
        check()
        parts = imgcut!!.cut(getNum())
    }

    fun inPool(): Boolean {
        return id.pack != null && id.pack == "_local"
    }

    fun isSaved(): Boolean {
        return saved
    }

    override fun load() {
        try {
            super.load()
            history("initial")
        } catch (e: Exception) {
            e.printStackTrace()
            CommonStatic.def.exit(false)
        }
        validate()
    }

    fun localize(pack: String) {
        if (id.pack == ResourceLocation.Companion.LOCAL) map().remove(id.id)
        val saver = SourceAnimSaver(id, this)
        saver.delete()
        id.pack = pack
        if (id.pack == ResourceLocation.Companion.LOCAL) map()[str] = this
        saver.saveAll()
    }

    fun merge(a: AnimCE, x: Int, y: Int) {
        val ic0: ImgCut = imgcut!!
        val ic1: ImgCut = a.imgcut!!
        val icn: Int = ic0.n
        ic0.n += ic1.n
        ic0.cuts = Arrays.copyOf<IntArray>(ic0.cuts, ic0.n)
        for (i in 0 until icn) ic0.cuts.get(i) = ic0.cuts.get(i)!!.clone()
        ic0.strs = Arrays.copyOf<String>(ic0.strs, ic0.n)
        for (i in 0 until ic1.n) {
            ic0.cuts.get(i + icn) = ic1.cuts.get(i)!!.clone()
            val data: IntArray = ic0.cuts.get(i + icn)
            data[0] += x
            data[1] += y
            ic0.strs.get(i + icn) = ic1.strs.get(i)
        }
        val mm0: MaModel = mamodel!!
        val mm1: MaModel = a.mamodel!!
        val mmn: Int = mm0.n
        mm0.n += mm1.n
        mm0.parts = Arrays.copyOf<IntArray>(mm0.parts, mm0.n)
        for (i in 0 until mmn) mm0.parts.get(i) = mm0.parts.get(i)!!.clone()
        mm0.strs0 = Arrays.copyOf<String>(mm0.strs0, mm0.n)
        val fir: IntArray = mm0.parts.get(0)
        for (i in 0 until mm1.n) {
            mm0.parts.get(i + mmn) = mm1.parts.get(i)!!.clone()
            val data: IntArray = mm0.parts.get(i + mmn)
            if (data[0] != -1) data[0] += mmn else {
                data[0] = 0
                data[8] = data[8] * 1000 / fir[8]
                data[9] = data[9] * 1000 / fir[9]
                data[4] = data[6] * data[8] / 1000 - fir[6]
                data[5] = data[7] * data[9] / 1000 - fir[7]
            }
            data[2] += icn
            mm0.strs0.get(i + mmn) = mm1.strs0.get(i)
        }
        for (i in 0..6) {
            val ma0: MaAnim = anims.get(i)
            val ma1: MaAnim = a.anims.get(i)
            val man: Int = ma0.n
            ma0.n += ma1.n
            ma0.parts = Arrays.copyOf(ma0.parts, ma0.n)
            for (j in 0 until man) ma0.parts.get(j) = ma0.parts.get(j)!!.clone()
            for (j in 0 until ma1.n) {
                ma0.parts.get(j + man) = ma1.parts.get(j)!!.clone()
                val p: Part = ma0.parts.get(j + man)
                p.ints[0] += mmn
                if (p.ints[1] == 2) for (data in p.moves) data[1] += icn
            }
        }
    }

    fun reloImg() {
        setNum(loader.loader.getNum())
        ICedited()
    }

    fun renameTo(str: String) {
        if (id.pack == ResourceLocation.Companion.LOCAL) map().remove(id.id)
        val saver = SourceAnimSaver(id, this)
        saver.delete()
        id.id = str
        if (id.pack == ResourceLocation.Companion.LOCAL) map()[str] = this
        saver.saveAll()
        unSave("rename (not applicapable for undo)")
    }

    fun resize(d: Double) {
        for (l in imgcut!!.cuts) for (i in l!!.indices) (l!![i] *= d).toInt()
        (mamodel!!.parts.get(0)!!.get(8) /= d).toInt()
        (mamodel!!.parts.get(0)!!.get(9) /= d).toInt()
        for (l in mamodel!!.parts) {
            (l!![4] *= d).toInt()
            (l!![5] *= d).toInt()
            (l!![6] *= d).toInt()
            (l!![7] *= d).toInt()
        }
        for (ma in anims) for (p in ma.parts) if (p!!.ints[1] >= 4 && p!!.ints[1] <= 7) for (x in p!!.moves) (x[1] *= d).toInt()
        unSave("resize")
    }

    fun restore() {
        history.pop()
        var `is`: InStream = history.peek().data.translate()
        imgcut!!.restore(`is`)
        ICedited()
        mamodel!!.restore(`is`)
        var n: Int = `is`.nextInt()
        anims = arrayOfNulls<MaAnim>(n)
        for (i in 0 until n) {
            anims.get(i) = MaAnim()
            anims.get(i).restore(`is`)
        }
        `is` = history.peek().mms.translate()
        n = `is`.nextInt()
        for (i in 0 until n) {
            val ind: Int = `is`.nextInt()
            val `val`: Int = `is`.nextInt()
            if (ind >= 0 && ind < mamodel!!.n) mamodel!!.status.put(mamodel!!.parts.get(ind), `val`)
        }
        saved = false
    }

    override fun revert() {
        super.revert()
        unSave("revert")
    }

    fun save() {
        if (!loaded || isSaved()) return
        saved = true
        SourceAnimSaver(id, this).saveAll()
    }

    fun saveIcon() {
        SourceAnimSaver(id, this).saveIconDisplay()
    }

    fun saveImg() {
        SourceAnimSaver(id, this).saveSprite()
    }

    fun saveUni() {
        SourceAnimSaver(id, this).saveIconDeploy()
    }

    fun setEdi(uni: VImg?) {
        loader.setEdi(uni)
    }

    fun setNum(fimg: FakeImage?) {
        loader.setNum(fimg)
        if (loaded) ICedited()
    }

    fun setUni(uni: VImg?) {
        loader.setUni(uni)
    }

    fun unSave(str: String) {
        saved = false
        history(str)
        if (link != null) link.review()
    }

    fun updateStatus() {
        partial()
        val mms: OutStream = OutStream.Companion.getIns()
        mms.writeInt(mamodel!!.status.size)
        mamodel!!.status.forEach(BiConsumer<IntArray, Int> { d: IntArray, s: Int? ->
            var ind = -1
            for (i in 0 until mamodel!!.n) if (mamodel!!.parts.get(i) == d) ind = i
            mms.writeInt(ind)
            mms.writeInt(s)
        })
        mms.terminate()
        history.peek().mms = mms
    }

    protected override fun partial() {
        super.partial()
        standardize()
    }

    private fun history(str: String) {
        partial()
        val os: OutStream = OutStream.Companion.getIns()
        imgcut.write(os)
        mamodel.write(os)
        os.writeInt(anims.size)
        for (ma in anims) ma.write(os)
        os.terminate()
        val h: common.util.anim.AnimCE.History = common.util.anim.AnimCE.History(str, os)
        history.push(h)
        updateStatus()
    }

    private fun standardize() {
        if (mamodel!!.parts.size == 0 || mamodel!!.confs.size == 0) return
        val dat: IntArray = mamodel!!.parts.get(0)
        val con: IntArray = mamodel!!.confs.get(0)
        dat[6] -= con[2]
        dat[7] -= con[3]
        con[3] = 0
        con[2] = con[3]
        val std: IntArray = mamodel!!.ints
        for (data in mamodel!!.parts) {
            data!![8] = data!![8] * 1000 / std[0]
            data!![9] = data!![9] * 1000 / std[0]
            data!![10] = data!![10] * 3600 / std[1]
            data!![11] = data!![11] * 1000 / std[2]
        }
        for (ma in anims) for (p in ma.parts) {
            if (p!!.ints[1] >= 8 && p!!.ints[1] <= 10) for (data in p!!.moves) data[1] = data[1] * 1000 / std[0]
            if (p!!.ints[1] == 11) for (data in p!!.moves) data[1] = data[1] * 3600 / std[1]
            if (p!!.ints[1] == 12) for (data in p!!.moves) data[1] = data[1] * 1000 / std[2]
        }
        std[0] = 1000
        std[1] = 3600
        std[2] = 1000
    }

    companion object {
        private const val REG_LOCAL_ANIM = "local_animation"
        fun getAvailable(string: String): String {
            // FIXME Auto-generated method stub
            return string
        }

        fun map(): MutableMap<String, AnimCE> {
            return UserProfile.Companion.getRegister<AnimCE>(REG_LOCAL_ANIM, AnimCE::class.java)
        }
    }
}