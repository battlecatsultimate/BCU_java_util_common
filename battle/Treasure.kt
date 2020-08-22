package common.battleimport

import common.util.Data

com.google.api.client.json.jackson2.JacksonFactoryimport com.google.api.services.drive.DriveScopesimport com.google.api.client.util.store.FileDataStoreFactoryimport com.google.api.client.http.HttpTransportimport com.google.api.services.drive.Driveimport kotlin.Throwsimport java.io.IOExceptionimport io.drive.DriveUtilimport java.io.FileNotFoundExceptionimport java.io.FileInputStreamimport com.google.api.client.googleapis.auth.oauth2.GoogleClientSecretsimport java.io.InputStreamReaderimport com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlowimport com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledAppimport com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiverimport com.google.api.client.googleapis.javanet.GoogleNetHttpTransportimport kotlin.jvm.JvmStaticimport io.drive.DrvieInitimport com.google.api.client.http.javanet.NetHttpTransportimport com.google.api.services.drive.model.FileListimport java.io.BufferedInputStreamimport java.io.FileOutputStreamimport com.google.api.client.googleapis.media.MediaHttpDownloaderimport io.WebFileIOimport io.BCJSONimport page.LoadPageimport org.json.JSONObjectimport org.json.JSONArrayimport main.MainBCUimport main.Optsimport common.CommonStaticimport java.util.TreeMapimport java.util.Arraysimport java.io.BufferedReaderimport io.BCMusicimport common.util.stage.Musicimport io.BCPlayerimport java.util.HashMapimport javax.sound.sampled.Clipimport java.io.ByteArrayInputStreamimport javax.sound.sampled.AudioInputStreamimport javax.sound.sampled.AudioSystemimport javax.sound.sampled.DataLineimport javax.sound.sampled.FloatControlimport javax.sound.sampled.LineEventimport com.google.api.client.googleapis.media.MediaHttpDownloaderProgressListenerimport com.google.api.client.googleapis.media.MediaHttpDownloader.DownloadStateimport common.io.DataIOimport io.BCUReaderimport common.io.InStreamimport com.google.gson.JsonElementimport common.io.json.JsonDecoderimport com.google.gson.JsonObjectimport page.MainFrameimport page.view.ViewBox.Confimport page.MainLocaleimport page.battle.BattleInfoPageimport page.support.Exporterimport page.support.Importerimport common.pack.Context.ErrTypeimport common.util.stage.MapColcimport common.util.stage.MapColc.DefMapColcimport common.util.lang.MultiLangContimport common.util.stage.StageMapimport common.util.unit.Enemyimport io.BCUWriterimport java.text.SimpleDateFormatimport java.io.PrintStreamimport common.io.OutStreamimport common.battle.BasisSetimport res.AnimatedGifEncoderimport java.awt.image.BufferedImageimport javax.imageio.ImageIOimport java.security.MessageDigestimport java.security.NoSuchAlgorithmExceptionimport common.io.json.JsonEncoderimport java.io.FileWriterimport com.google.api.client.http.GenericUrlimport org.apache.http.impl .client.CloseableHttpClientimport org.apache.http.impl .client.HttpClientsimport org.apache.http.client.methods.HttpPostimport org.apache.http.entity.mime.content.FileBodyimport org.apache.http.entity.mime.MultipartEntityBuilderimport org.apache.http.entity.mime.HttpMultipartModeimport org.apache.http.HttpEntityimport org.apache.http.util.EntityUtilsimport com.google.api.client.http.HttpRequestInitializerimport com.google.api.client.http.HttpBackOffUnsuccessfulResponseHandlerimport com.google.api.client.util.ExponentialBackOffimport com.google.api.client.http.HttpBackOffIOExceptionHandlerimport res.NeuQuantimport res.LZWEncoderimport java.io.BufferedOutputStreamimport java.awt.Graphics2Dimport java.awt.image.DataBufferByteimport common.system.fake.FakeImageimport utilpc.awt.FIBIimport jogl.util.AmbImageimport common.system.files.VFileimport jogl.util.GLImageimport com.jogamp.opengl.util.texture.TextureDataimport common.system.Pimport com.jogamp.opengl.util.texture.TextureIOimport jogl.GLStaticimport com.jogamp.opengl.util.texture.awt.AWTTextureIOimport java.awt.AlphaCompositeimport common.system.fake.FakeImage.Markerimport jogl.util.GLGraphicsimport com.jogamp.opengl.GL2import jogl.util.GeoAutoimport com.jogamp.opengl.GL2ES3import com.jogamp.opengl.GLimport common.system.fake.FakeGraphicsimport common.system.fake.FakeTransformimport jogl.util.ResManagerimport jogl.util.GLGraphics.GeomGimport jogl.util.GLGraphics.GLCimport jogl.util.GLGraphics.GLTimport com.jogamp.opengl.GL2ES2import com.jogamp.opengl.util.glsl.ShaderCodeimport com.jogamp.opengl.util.glsl.ShaderProgramimport com.jogamp.opengl.GLExceptionimport jogl.StdGLCimport jogl.Tempimport common.util.anim.AnimUimport common.util.anim.EAnimUimport jogl.util.GLIBimport javax.swing.JFrameimport common.util.anim.AnimCEimport common.util.anim.AnimU.UTypeimport com.jogamp.opengl.util.FPSAnimatorimport com.jogamp.opengl.GLEventListenerimport com.jogamp.opengl.GLAutoDrawableimport page.awt.BBBuilderimport page.battle.BattleBox.OuterBoximport common.battle.SBCtrlimport page.battle.BattleBoximport jogl.GLBattleBoximport common.battle.BattleFieldimport page.anim.IconBoximport jogl.GLIconBoximport jogl.GLBBRecdimport page.awt.RecdThreadimport page.view.ViewBoximport jogl.GLViewBoximport page.view.ViewBox.Controllerimport java.awt.AWTExceptionimport page.battle.BBRecdimport jogl.GLRecorderimport com.jogamp.opengl.GLProfileimport com.jogamp.opengl.GLCapabilitiesimport page.anim.IconBox.IBCtrlimport page.anim.IconBox.IBConfimport page.view.ViewBox.VBExporterimport jogl.GLRecdBImgimport page.JTGimport jogl.GLCstdimport jogl.GLVBExporterimport common.util.anim.EAnimIimport page.RetFuncimport page.battle.BattleBox.BBPainterimport page.battle.BBCtrlimport javax.swing.JOptionPaneimport kotlin.jvm.Strictfpimport main.Invimport javax.swing.SwingUtilitiesimport java.lang.InterruptedExceptionimport utilpc.UtilPC.PCItrimport utilpc.awt.PCIBimport jogl.GLBBBimport page.awt.AWTBBBimport utilpc.Themeimport page.MainPageimport common.io.assets.AssetLoaderimport common.pack.Source.Workspaceimport common.io.PackLoader.ZipDesc.FileDescimport common.io.assets.Adminimport page.awt.BattleBoxDefimport page.awt.IconBoxDefimport page.awt.BBRecdAWTimport page.awt.ViewBoxDefimport org.jcodec.api.awt.AWTSequenceEncoderimport page.awt.RecdThread.PNGThreadimport page.awt.RecdThread.MP4Threadimport page.awt.RecdThread.GIFThreadimport java.awt.GradientPaintimport utilpc.awt.FG2Dimport page.anim.TreeContimport javax.swing.JTreeimport javax.swing.event.TreeExpansionListenerimport common.util.anim.MaModelimport javax.swing.tree.DefaultMutableTreeNodeimport javax.swing.event.TreeExpansionEventimport java.util.function.IntPredicateimport javax.swing.tree.DefaultTreeModelimport common.util.anim.EAnimDimport page.anim.AnimBoximport utilpc.PPimport common.CommonStatic.BCAuxAssetsimport common.CommonStatic.EditLinkimport page.JBTNimport page.anim.DIYViewPageimport page.anim.ImgCutEditPageimport page.anim.MaModelEditPageimport page.anim.MaAnimEditPageimport page.anim.EditHeadimport java.awt.event.ActionListenerimport page.anim.AbEditPageimport common.util.anim.EAnimSimport page.anim.ModelBoximport common.util.anim.ImgCutimport page.view.AbViewPageimport javax.swing.JListimport javax.swing.JScrollPaneimport javax.swing.JComboBoximport utilpc.UtilPCimport javax.swing.event.ListSelectionListenerimport javax.swing.event.ListSelectionEventimport common.system.VImgimport page.support.AnimLCRimport page.support.AnimTableimport common.util.anim.MaAnimimport java.util.EventObjectimport javax.swing.text.JTextComponentimport page.anim.PartEditTableimport javax.swing.ListSelectionModelimport page.support.AnimTableTHimport page.JTFimport utilpc.ReColorimport page.anim.ImgCutEditTableimport page.anim.SpriteBoximport page.anim.SpriteEditPageimport java.awt.event.FocusAdapterimport java.awt.event.FocusEventimport common.pack.PackData.UserPackimport utilpc.Algorithm.SRResultimport page.anim.MaAnimEditTableimport javax.swing.JSliderimport java.awt.event.MouseWheelEventimport common.util.anim.EPartimport javax.swing.event.ChangeEventimport page.anim.AdvAnimEditPageimport javax.swing.BorderFactoryimport page.JLimport javax.swing.ImageIconimport page.anim.MMTreeimport javax.swing.event.TreeSelectionListenerimport javax.swing.event.TreeSelectionEventimport page.support.AbJTableimport page.anim.MaModelEditTableimport page.info.edit.ProcTableimport page.info.edit.ProcTable.AtkProcTableimport page.info.edit.SwingEditorimport page.info.edit.ProcTable.MainProcTableimport page.support.ListJtfPolicyimport page.info.edit.SwingEditor.SwingEGimport common.util.Data.Procimport java.lang.Runnableimport javax.swing.JComponentimport page.info.edit.LimitTableimport page.pack.CharaGroupPageimport page.pack.LvRestrictPageimport javax.swing.SwingConstantsimport common.util.lang.Editors.EditorGroupimport common.util.lang.Editors.EdiFieldimport common.util.lang.Editorsimport common.util.lang.ProcLangimport page.info.edit.EntityEditPageimport common.util.lang.Editors.EditorSupplierimport common.util.lang.Editors.EditControlimport page.info.edit.SwingEditor.IntEditorimport page.info.edit.SwingEditor.BoolEditorimport page.info.edit.SwingEditor.IdEditorimport page.SupPageimport common.util.unit.AbEnemyimport common.pack.IndexContainer.Indexableimport common.pack.Context.SupExcimport common.battle.data .AtkDataModelimport utilpc.Interpretimport common.battle.data .CustomEntityimport page.info.filter.UnitEditBoximport common.battle.data .CustomUnitimport common.util.stage.SCGroupimport page.info.edit.SCGroupEditTableimport common.util.stage.SCDefimport page.info.filter.EnemyEditBoximport common.battle.data .CustomEnemyimport page.info.StageFilterPageimport page.view.BGViewPageimport page.view.CastleViewPageimport page.view.MusicPageimport common.util.stage.CastleImgimport common.util.stage.CastleListimport java.text.DecimalFormatimport common.util.stage.Recdimport common.util.stage.MapColc.PackMapColcimport page.info.edit.StageEditTableimport page.support.ReorderListimport page.info.edit.HeadEditTableimport page.info.filter.EnemyFindPageimport page.battle.BattleSetupPageimport page.info.edit.AdvStEditPageimport page.battle.StRecdPageimport page.info.edit.LimitEditPageimport page.support.ReorderListenerimport common.util.pack.Soulimport page.info.edit.AtkEditTableimport page.info.filter.UnitFindPageimport common.battle.Basisimport common.util.Data.Proc.IMUimport javax.swing.DefaultComboBoxModelimport common.util.Animableimport common.util.pack.Soul.SoulTypeimport page.view.UnitViewPageimport page.view.EnemyViewPageimport page.info.edit.SwingEditor.EditCtrlimport page.support.Reorderableimport page.info.EnemyInfoPageimport common.util.unit.EneRandimport page.pack.EREditPageimport page.support.InTableTHimport page.support.EnemyTCRimport javax.swing.DefaultListCellRendererimport page.info.filter.UnitListTableimport page.info.filter.UnitFilterBoximport page.info.filter.EnemyListTableimport page.info.filter.EnemyFilterBoximport page.info.filter.UFBButtonimport page.info.filter.UFBListimport common.battle.data .MaskUnitimport javax.swing.AbstractButtonimport page.support.SortTableimport page.info.UnitInfoPageimport page.support.UnitTCRimport page.info.filter.EFBButtonimport page.info.filter.EFBListimport common.util.stage.LvRestrictimport common.util.stage.CharaGroupimport page.info.StageTableimport page.info.TreaTableimport javax.swing.JPanelimport page.info.UnitInfoTableimport page.basis.BasisPageimport kotlin.jvm.JvmOverloadsimport page.info.EnemyInfoTableimport common.util.stage.RandStageimport page.info.StagePageimport page.info.StageRandPageimport common.util.unit.EFormimport page.pack.EREditTableimport common.util.EREntimport common.pack.FixIndexListimport page.support.UnitLCRimport page.pack.RecdPackPageimport page.pack.CastleEditPageimport page.pack.BGEditPageimport page.pack.CGLREditPageimport common.pack.Source.ZipSourceimport page.info.edit.EnemyEditPageimport page.info.edit.StageEditPageimport page.info.StageViewPageimport page.pack.UnitManagePageimport page.pack.MusicEditPageimport page.battle.AbRecdPageimport common.system.files.VFileRootimport java.awt.Desktopimport common.pack.PackDataimport common.util.unit.UnitLevelimport page.info.edit.FormEditPageimport common.util.anim.AnimIimport common.util.anim.AnimI.AnimTypeimport common.util.anim.AnimDimport common.battle.data .Orbimport page.basis.LineUpBoximport page.basis.LubContimport common.battle.BasisLUimport page.basis.ComboListTableimport page.basis.ComboListimport page.basis.NyCasBoximport page.basis.UnitFLUPageimport common.util.unit.Comboimport page.basis.LevelEditPageimport common.util.pack.NyCastleimport common.battle.LineUpimport common.system.SymCoordimport java.util.TreeSetimport page.basis.OrbBoximport javax.swing.table.DefaultTableCellRendererimport javax.swing.JTableimport common.CommonStatic.BattleConstimport common.battle.StageBasisimport common.util.ImgCoreimport common.battle.attack.ContAbimport common.battle.entity.EAnimContimport common.battle.entity.WaprContimport page.battle.RecdManagePageimport page.battle.ComingTableimport common.util.stage.EStageimport page.battle.EntityTableimport common.battle.data .MaskEnemyimport common.battle.SBRplyimport common.battle.entity.AbEntityimport page.battle.RecdSavePageimport page.LocCompimport page.LocSubCompimport javax.swing.table.TableModelimport page.support.TModelimport javax.swing.event.TableModelListenerimport javax.swing.table.DefaultTableColumnModelimport javax.swing.JFileChooserimport javax.swing.filechooser.FileNameExtensionFilterimport javax.swing.TransferHandlerimport java.awt.datatransfer.Transferableimport java.awt.datatransfer.DataFlavorimport javax.swing.DropModeimport javax.swing.TransferHandler.TransferSupportimport java.awt.dnd.DragSourceimport java.awt.datatransfer.UnsupportedFlavorExceptionimport common.system.Copableimport page.support.AnimTransferimport javax.swing.DefaultListModelimport page.support.InListTHimport java.awt.FocusTraversalPolicyimport javax.swing.JTextFieldimport page.CustomCompimport javax.swing.JToggleButtonimport javax.swing.JButtonimport javax.swing.ToolTipManagerimport javax.swing.JRootPaneimport javax.swing.JProgressBarimport page.ConfigPageimport page.view.EffectViewPageimport page.pack.PackEditPageimport page.pack.ResourcePageimport javax.swing.WindowConstantsimport java.awt.event.AWTEventListenerimport java.awt.AWTEventimport java.awt.event.ComponentAdapterimport java.awt.event.ComponentEventimport java.util.ConcurrentModificationExceptionimport javax.swing.plaf.FontUIResourceimport java.util.Enumerationimport javax.swing.UIManagerimport common.CommonStatic.FakeKeyimport page.LocSubComp.LocBinderimport page.LSCPopimport java.awt.BorderLayoutimport java.awt.GridLayoutimport javax.swing.JTextPaneimport page.TTTimport java.util.ResourceBundleimport java.util.MissingResourceExceptionimport java.util.Localeimport common.io.json.Test.JsonTest_2import common.pack.PackData.PackDescimport common.io.PackLoaderimport common.io.PackLoader.Preloadimport common.io.PackLoader.ZipDescimport common.io.json.Testimport common.io.json.JsonClassimport common.io.json.JsonFieldimport common.io.json.JsonField.GenTypeimport common.io.json.Test.JsonTest_0.JsonDimport common.io.json.JsonClass.RTypeimport java.util.HashSetimport common.io.json.JsonDecoder.OnInjectedimport common.io.json.JsonField.IOTypeimport common.io.json.JsonExceptionimport common.io.json.JsonClass.NoTagimport common.io.json.JsonField.SerTypeimport common.io.json.JsonClass.WTypeimport kotlin.reflect.KClassimport com.google.gson.JsonArrayimport common.io.assets.Admin.StaticPermittedimport common.io.json.JsonClass.JCGenericimport common.io.json.JsonClass.JCGetterimport com.google.gson.JsonPrimitiveimport com.google.gson.JsonNullimport common.io.json.JsonClass.JCIdentifierimport java.lang.ClassNotFoundExceptionimport common.io.assets.AssetLoader.AssetHeaderimport common.io.assets.AssetLoader.AssetHeader.AssetEntryimport common.io.InStreamDefimport common.io.BCUExceptionimport java.io.UnsupportedEncodingExceptionimport common.io.OutStreamDefimport javax.crypto.Cipherimport javax.crypto.spec.IvParameterSpecimport javax.crypto.spec.SecretKeySpecimport common.io.PackLoader.FileSaverimport common.system.files.FDByteimport common.io.json.JsonClass.JCConstructorimport common.io.PackLoader.FileLoader.FLStreamimport common.io.PackLoader.PatchFileimport java.lang.NullPointerExceptionimport java.lang.IndexOutOfBoundsExceptionimport common.io.MultiStreamimport java.io.RandomAccessFileimport common.io.MultiStream.TrueStreamimport java.lang.RuntimeExceptionimport common.pack.Source.ResourceLocationimport common.pack.Source.AnimLoaderimport common.pack.Source.SourceAnimLoaderimport common.util.anim.AnimCIimport common.system.files.FDFileimport common.pack.IndexContainerimport common.battle.data .PCoinimport common.util.pack.EffAnimimport common.battle.data .DataEnemyimport common.util.stage.Limit.DefLimitimport common.pack.IndexContainer.Reductorimport common.pack.FixIndexList.FixIndexMapimport common.pack.VerFixer.IdFixerimport common.pack.IndexContainer.IndexContimport common.pack.IndexContainer.ContGetterimport common.util.stage.CastleList.PackCasListimport common.util.Data.Proc.THEMEimport common.CommonStatic.ImgReaderimport common.pack.VerFixerimport common.pack.VerFixer.VerFixerExceptionimport java.lang.NumberFormatExceptionimport common.pack.Source.SourceAnimSaverimport common.pack.VerFixer.EnemyFixerimport common.pack.VerFixer.PackFixerimport common.pack.PackData.DefPackimport java.util.function.BiConsumerimport common.util.BattleStaticimport common.util.anim.AnimU.ImageKeeperimport common.util.anim.AnimCE.AnimCELoaderimport common.util.anim.AnimCI.AnimCIKeeperimport common.util.anim.AnimUD.DefImgLoaderimport common.util.BattleObjimport common.util.Data.Proc.ProcItemimport common.util.lang.ProcLang.ItemLangimport common.util.lang.LocaleCenter.Displayableimport common.util.lang.Editors.DispItemimport common.util.lang.LocaleCenter.ObjBinderimport common.util.lang.LocaleCenter.ObjBinder.BinderFuncimport common.util.Data.Proc.PROBimport org.jcodec.common.tools.MathUtilimport common.util.Data.Proc.PTimport common.util.Data.Proc.PTDimport common.util.Data.Proc.PMimport common.util.Data.Proc.WAVEimport common.util.Data.Proc.WEAKimport common.util.Data.Proc.STRONGimport common.util.Data.Proc.BURROWimport common.util.Data.Proc.REVIVEimport common.util.Data.Proc.SUMMONimport common.util.Data.Proc.MOVEWAVEimport common.util.Data.Proc.POISONimport common.util.Data.Proc.CRITIimport common.util.Data.Proc.VOLCimport common.util.Data.Proc.ARMORimport common.util.Data.Proc.SPEEDimport java.util.LinkedHashMapimport common.util.lang.LocaleCenter.DisplayItemimport common.util.lang.ProcLang.ProcLangStoreimport common.util.lang.Formatter.IntExpimport common.util.lang.Formatter.RefObjimport common.util.lang.Formatter.BoolExpimport common.util.lang.Formatter.BoolElemimport common.util.lang.Formatter.IElemimport common.util.lang.Formatter.Contimport common.util.lang.Formatter.Elemimport common.util.lang.Formatter.RefElemimport common.util.lang.Formatter.RefFieldimport common.util.lang.Formatter.RefFuncimport common.util.lang.Formatter.TextRefimport common.util.lang.Formatter.CodeBlockimport common.util.lang.Formatter.TextPlainimport common.util.unit.Unit.UnitInfoimport common.util.lang.MultiLangCont.MultiLangStaticsimport common.util.pack.EffAnim.EffTypeimport common.util.pack.EffAnim.ArmorEffimport common.util.pack.EffAnim.BarEneEffimport common.util.pack.EffAnim.BarrierEffimport common.util.pack.EffAnim.DefEffimport common.util.pack.EffAnim.WarpEffimport common.util.pack.EffAnim.ZombieEffimport common.util.pack.EffAnim.KBEffimport common.util.pack.EffAnim.SniperEffimport common.util.pack.EffAnim.VolcEffimport common.util.pack.EffAnim.SpeedEffimport common.util.pack.EffAnim.WeakUpEffimport common.util.pack.EffAnim.EffAnimStoreimport common.util.pack.NyCastle.NyTypeimport common.util.pack.WaveAnimimport common.util.pack.WaveAnim.WaveTypeimport common.util.pack.Background.BGWvTypeimport common.util.unit.Form.FormJsonimport common.system.BasedCopableimport common.util.anim.AnimUDimport common.battle.data .DataUnitimport common.battle.entity.EUnitimport common.battle.entity.EEnemyimport common.util.EntRandimport common.util.stage.Recd.Waitimport java.lang.CloneNotSupportedExceptionimport common.util.stage.StageMap.StageMapInfoimport common.util.stage.Stage.StageInfoimport common.util.stage.Limit.PackLimitimport common.util.stage.MapColc.ClipMapColcimport common.util.stage.CastleList.DefCasListimport common.util.stage.MapColc.StItrimport common.util.Data.Proc.IntType.BitCountimport common.util.CopRandimport common.util.LockGLimport java.lang.IllegalAccessExceptionimport common.battle.data .MaskAtkimport common.battle.data .DefaultDataimport common.battle.data .DataAtkimport common.battle.data .MaskEntityimport common.battle.data .DataEntityimport common.battle.attack.AtkModelAbimport common.battle.attack.AttackAbimport common.battle.attack.AttackSimpleimport common.battle.attack.AttackWaveimport common.battle.entity.Cannonimport common.battle.attack.AttackVolcanoimport common.battle.attack.ContWaveAbimport common.battle.attack.ContWaveDefimport common.battle.attack.AtkModelEntityimport common.battle.entity.EntContimport common.battle.attack.ContMoveimport common.battle.attack.ContVolcanoimport common.battle.attack.ContWaveCanonimport common.battle.attack.AtkModelEnemyimport common.battle.attack.AtkModelUnitimport common.battle.attack.AttackCanonimport common.battle.entity.EUnit.OrbHandlerimport common.battle.entity.Entity.AnimManagerimport common.battle.entity.Entity.AtkManagerimport common.battle.entity.Entity.ZombXimport common.battle.entity.Entity.KBManagerimport common.battle.entity.Entity.PoisonTokenimport common.battle.entity.Entity.WeakTokenimport common.battle.Treasureimport common.battle.MirrorSetimport common.battle.Releaseimport common.battle.ELineUpimport common.battle.entity.Sniperimport common.battle.entity.ECastleimport java.util.Dequeimport common.CommonStatic.Itfimport java.lang.Characterimport common.CommonStatic.ImgWriterimport utilpc.awt.FTATimport utilpc.awt.Blenderimport java.awt.RenderingHintsimport utilpc.awt.BIBuilderimport java.awt.CompositeContextimport java.awt.image.Rasterimport java.awt.image.WritableRasterimport utilpc.ColorSetimport utilpc.OggTimeReaderimport utilpc.UtilPC.PCItr.MusicReaderimport utilpc.UtilPC.PCItr.PCALimport javax.swing.UIManager.LookAndFeelInfoimport java.lang.InstantiationExceptionimport javax.swing.UnsupportedLookAndFeelExceptionimport utilpc.Algorithm.ColorShiftimport utilpc.Algorithm.StackRect
@JsonClass(read = RType.FILL)
class Treasure : Data {
    val b: Basis

    @JsonField(gen = GenType.FILL)
    var tech = IntArray(Data.Companion.LV_TOT)

    @JsonField(gen = GenType.FILL)
    var trea = IntArray(Data.Companion.T_TOT)

    @JsonField(gen = GenType.FILL)
    var bslv = IntArray(Data.Companion.BASE_TOT)

    @JsonField(gen = GenType.FILL)
    var fruit = IntArray(7)

    @JsonField(gen = GenType.FILL)
    var gods = IntArray(3)

    @JsonField
    var alien = 0

    @JsonField
    var star = 0

    /** new Treasure object  */
    constructor(bas: Basis) {
        b = bas
        `zread$000000`()
    }

    /** read Treasure from data  */
    constructor(bas: Basis, ver: Int, `is`: InStream) {
        b = bas
        zread(ver, `is`)
    }

    /** copy Treasure object  */
    constructor(bas: Basis, t: Treasure) {
        b = bas
        tech = t.tech.clone()
        trea = t.trea.clone()
        fruit = t.fruit.clone()
        gods = t.gods.clone()
        alien = t.alien
        star = t.star
        bslv = t.bslv.clone()
    }

    /** get multiplication of non-starred alien  */
    fun getAlienMulti(): Double {
        return 7 - alien * 0.01
    }

    /** get cat attack multiplication  */
    fun getAtkMulti(): Double {
        val ini = 1 + trea[Data.Companion.T_ATK] * 0.005
        val com: Double = 1 + b.getInc(Data.Companion.C_ATK) * 0.01
        return ini * com
    }

    /** get base health  */
    fun getBaseHealth(): Int {
        val t = tech[Data.Companion.LV_BASE]
        var base = if (t < 6) t * 1000 else if (t < 8) 5000 + (t - 5) * 2000 else 9000 + (t - 7) * 3000
        base += trea[Data.Companion.T_BASE] * 70
        base += if (bslv[0] > 10) 36000 + 4000 * (bslv[0] - 10) else 3600 * bslv[0]
        return base * (100 + b.getInc(Data.Companion.C_BASE)) / 100
    }

    /** get normal canon attack  */
    fun getCanonAtk(): Int {
        val base = 50 + tech[Data.Companion.LV_CATK] * 50 + trea[Data.Companion.T_CATK] * 5
        return base * (100 + b.getInc(Data.Companion.C_C_ATK)) / 100
    }

    /** get special canon data 1  */
    fun getCanonMulti(type: Int): Double {
        if (type == 2) return if (bslv[2] > 10) 298 + 3.2 * (bslv[2] - 10) else 100 + 19.8 * bslv[2] else if (type == 3) return if (bslv[3] > 10) 39 + 0.9 * (bslv[3] - 10) else 30 + 0.9 * bslv[3] else if (type == 4) return if (bslv[4] > 10) (200 + 15 * (bslv[4] - 10)).toDouble() else (110 + 9 * bslv[4]).toDouble() else if (type == 5) return if (bslv[5] > 10) 25 + 7.5 * (bslv[5] - 10) else (5 + 2 * bslv[5]).toDouble() else if (type == -5) return if (bslv[5] > 10) (150 + 15 * (bslv[5] - 10)).toDouble() else (100 + 5 * bslv[5]).toDouble() else if (type == 6) return if (bslv[6] > 10) (50 + 5 * (bslv[6] - 10)).toDouble() else (30 + 2 * bslv[6]).toDouble()
        return 0
    }

    /** get special canon data 2, usually proc time  */
    fun getCanonProcTime(type: Int): Int {
        if (type == 1) return if (bslv[1] > 10) 50 + 5 * (bslv[1] - 10) else 30 + 2 * bslv[1] else if (type == 2) return if (bslv[2] > 10) 90 + 9 * (bslv[2] - 10) / 2 else 60 + 3 * bslv[2] else if (type == 3) return if (bslv[3] > 10) 30 + 3 * (bslv[3] - 10) else 15 + 3 * bslv[3] / 2 else if (type == 5) return if (bslv[5] > 10) 45 + 3 * (bslv[3] - 10) / 2 else 30 + 3 * bslv[3] / 2 else if (type == 6) return if (bslv[6] > 10) 250 + 15 * (bslv[6] - 10) else 200 + 5 * bslv[6] else if (type == 7) return if (bslv[7] > 10) 60 + 6 * (bslv[7] - 10) else 33 + 3 * bslv[7]
        return 0
    }

    /** get cat health multiplication  */
    fun getDefMulti(): Double {
        val ini = 1 + trea[Data.Companion.T_DEF] * 0.005
        val com: Double = 1 + b.getInc(Data.Companion.C_DEF) * 0.01
        return ini * com
    }

    /** get accounting multiplication  */
    fun getDropMulti(): Double {
        return (0.95 + 0.05 * tech[Data.Companion.LV_ACC] + 0.005 * trea[Data.Companion.T_ACC]) * (1 + b.getInc(Data.Companion.C_MEAR) * 0.01)
    }

    /** get EVA kill ability attack multiplication  */
    fun getEKAtk(): Double {
        return 0.05 * (100 + b.getInc(Data.Companion.C_EKILL))
    }

    /** get EVA kill ability reduce damage multiplication  */
    fun getEKDef(): Double {
        return 20.0 / (100 + b.getInc(Data.Companion.C_EKILL))
    }

    /** get processed cat cool down time  */
    fun getFinRes(ori: Int): Int {
        val dec: Double = 6 - tech[Data.Companion.LV_RES] * 6 - trea[Data.Companion.T_RES] * 0.3 - b.getInc(Data.Companion.C_RESP)
        return Math.max(60.0, ori + 10 + dec).toInt()
    }

    /** get maximum fruit of certain trait bitmask  */
    fun getFruit(type: Int): Double {
        var ans = 0.0
        if (type and Data.Companion.TB_RED != 0) ans = Math.max(ans, fruit[Data.Companion.T_RED])
        if (type and Data.Companion.TB_BLACK != 0) ans = Math.max(ans, fruit[Data.Companion.T_BLACK])
        if (type and Data.Companion.TB_ANGEL != 0) ans = Math.max(ans, fruit[Data.Companion.T_ANGEL])
        if (type and Data.Companion.TB_FLOAT != 0) ans = Math.max(ans, fruit[Data.Companion.T_FLOAT])
        if (type and Data.Companion.TB_ALIEN != 0) ans = Math.max(ans, fruit[Data.Companion.T_ALIEN])
        if (type and Data.Companion.TB_METAL != 0) ans = Math.max(ans, fruit[Data.Companion.T_METAL])
        if (type and Data.Companion.TB_ZOMBIE != 0) ans = Math.max(ans, fruit[Data.Companion.T_ZOMBIE])
        return ans * 0.01
    }

    /** get attack multiplication from strong against ability  */
    fun getGOODATK(type: Int): Double {
        val ini = 1.5 * (1 + 0.2 / 3 * getFruit(type))
        val com: Double = 1 + b.getInc(Data.Companion.C_GOOD) * 0.01
        return ini * com
    }

    /** get damage reduce multiplication from strong against ability  */
    fun getGOODDEF(type: Int): Double {
        val ini = 0.5 - 0.1 / 3 * getFruit(type)
        val com: Double = 1 - b.getInc(Data.Companion.C_GOOD) * 0.01
        return ini * com
    }

    /** get attack multiplication from massive damage ability  */
    fun getMASSIVEATK(type: Int): Double {
        val ini = 3 + 1.0 / 3 * getFruit(type)
        val com: Double = 1 + b.getInc(Data.Companion.C_MASSIVE) * 0.01
        return ini * com
    }

    /** get attack multiplication from super massive damage ability  */
    fun getMASSIVESATK(type: Int): Double {
        return 5 + 1.0 / 3 * getFruit(type)
    }

    /** get damage reduce multiplication from resistant ability  */
    fun getRESISTDEF(type: Int): Double {
        val ini = 0.25 - 0.05 / 3 * getFruit(type)
        val com: Double = 1 - b.getInc(Data.Companion.C_RESIST) * 0.01
        return ini * com
    }

    /** get damage reduce multiplication from super resistant ability  */
    fun getRESISTSDEF(type: Int): Double {
        return 1.0 / 6 - 1.0 / 126 * getFruit(type)
    }

    /** get reverse cat cool down time  */
    fun getRevRes(res: Int): Int {
        var res = res
        if (res < 60) res = 60
        val dec: Double = 6 - tech[Data.Companion.LV_RES] * 6 - trea[Data.Companion.T_RES] * 0.3 - b.getInc(Data.Companion.C_RESP)
        return (res - 10 - dec).toInt()
    }

    /** get multiplication of starred enemy  */
    fun getStarMulti(st: Int): Double {
        return if (st == 1) 16 - star * 0.01 else 11 - 0.1 * gods[st - 2]
    }

    /** get witch kill ability attack multiplication  */
    fun getWKAtk(): Double {
        return 0.05 * (100 + b.getInc(Data.Companion.C_WKILL))
    }

    /** get witch kill ability reduce damage multiplication  */
    fun getWKDef(): Double {
        return 10.0 / (100 + b.getInc(Data.Companion.C_WKILL))
    }

    fun getXPMult(): Double {
        val txp1 = trea[Data.Companion.T_XP1]
        val txp2 = trea[Data.Companion.T_XP2]
        val tm = txp1 * 0.005 + txp2 * 0.0025
        return 0.95 + tech[Data.Companion.LV_XP] * 0.05 + tm
    }

    /** get canon recharge time  */
    fun CanonTime(map: Int): Int {
        var base = 1503 + 50 * (tech[Data.Companion.LV_CATK] - tech[Data.Companion.LV_RECH])
        base -= if (trea[Data.Companion.T_RECH] <= 300) (1.5 * trea[Data.Companion.T_RECH]).toInt() else 3 * trea[Data.Companion.T_RECH] - 450
        base -= b.getInc(Data.Companion.C_C_SPE)
        base = Math.max(950, base + map * 450)
        return base
    }

    /** get the cost to upgrade worker cat  */
    fun getLvCost(lv: Int): Int {
        val t = tech[Data.Companion.LV_WORK]
        val base = if (t < 8) 30 + 10 * t else 20 * t - 40
        return base * lv
    }

    /** get wallet capacity  */
    fun getMaxMon(lv: Int): Int {
        var base = Math.max(25, 50 * tech[Data.Companion.LV_WALT])
        base = base * (1 + lv)
        base += trea[Data.Companion.T_WALT] * 10
        return base * (100 + b.getInc(Data.Companion.C_M_MAX)) / 100
    }

    /** get money increase rate  */
    fun getMonInc(lv: Int): Double {
        return (0.15 + 0.1 * tech[Data.Companion.LV_WORK]) * (1 + (lv - 1) * 0.1) + trea[Data.Companion.T_WORK] * 0.01
    }

    /** save data to file  */
    protected fun write(os: OutStream) {
        os.writeString("0.4.0")
        os.writeIntB(tech)
        os.writeIntB(trea)
        os.writeInt(alien)
        os.writeInt(star)
        os.writeIntB(fruit)
        os.writeIntB(gods)
        os.writeIntB(bslv)
    }

    /** read date from file, support multiple versions  */
    private fun zread(`val`: Int, `is`: InStream) {
        var `val` = `val`
        `zread$000000`()
        if (`val` >= 305) `val` = Data.Companion.getVer(`is`.nextString())
        if (`val` >= 400) `zread$000400`(`is`) else if (`val` >= 305) `zread$000305`(`is`) else if (`val` >= 304) `zread$000304`(`is`) else if (`val` >= 301) `zread$000301`(`is`) else if (`val` >= 203) `zread$000203`(`is`)
    }

    private fun `zread$000000`() {
        for (i in 0 until Data.Companion.LV_TOT) tech[i] = Data.Companion.MLV.get(i)
        for (i in 0 until Data.Companion.T_TOT) trea[i] = Data.Companion.MT.get(i)
        fruit[Data.Companion.T_ANGEL] = 300
        fruit[Data.Companion.T_FLOAT] = fruit[Data.Companion.T_ANGEL]
        fruit[Data.Companion.T_BLACK] = fruit[Data.Companion.T_FLOAT]
        fruit[Data.Companion.T_RED] = fruit[Data.Companion.T_BLACK]
        fruit[Data.Companion.T_ALIEN] = 300
        fruit[Data.Companion.T_ZOMBIE] = fruit[Data.Companion.T_ALIEN]
        fruit[Data.Companion.T_METAL] = fruit[Data.Companion.T_ZOMBIE]
        for (i in 0 until Data.Companion.BASE_TOT) bslv[i] = 20
        gods[2] = 100
        gods[1] = gods[2]
        gods[0] = gods[1]
        alien = 600
        star = 1500
    }

    private fun `zread$000203`(`is`: InStream) {
        for (i in 0..7) tech[i] = `is`.nextByte()
        for (i in 0..8) trea[i] = `is`.nextShort()
        alien = `is`.nextInt()
        star = `is`.nextInt()
        fruit = `is`.nextIntsB()
        gods = `is`.nextIntsB()
    }

    private fun `zread$000301`(`is`: InStream) {
        `zread$000203`(`is`)
        for (i in 0..4) bslv[i] = `is`.nextByte()
    }

    private fun `zread$000304`(`is`: InStream) {
        `zread$000203`(`is`)
        for (i in 0..5) bslv[i] = `is`.nextByte()
    }

    private fun `zread$000305`(`is`: InStream) {
        `zread$000203`(`is`)
        val temp: IntArray = `is`.nextIntsB()
        for (i in temp.indices) bslv[i] = temp[i]
    }

    private fun `zread$000400`(`is`: InStream) {
        val lv: IntArray = `is`.nextIntsB()
        val tr: IntArray = `is`.nextIntsB()
        for (i in 0 until Math.min(Data.Companion.LV_TOT, lv.size)) tech[i] = lv[i]
        for (i in 0 until Math.min(Data.Companion.T_TOT, tr.size)) trea[i] = tr[i]
        alien = `is`.nextInt()
        star = `is`.nextInt()
        fruit = `is`.nextIntsB()
        gods = `is`.nextIntsB()
        val bs: IntArray = `is`.nextIntsB()
        for (i in bs.indices) bslv[i] = bs[i]
    }
}