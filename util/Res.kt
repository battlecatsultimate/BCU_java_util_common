package common.utilimport

import common.util.Data
import common.util.Res

com.google.api.client.json.jackson2.JacksonFactoryimport com.google.api.services.drive.DriveScopesimport com.google.api.client.util.store.FileDataStoreFactoryimport com.google.api.client.http.HttpTransportimport com.google.api.services.drive.Driveimport kotlin.Throwsimport java.io.IOExceptionimport io.drive.DriveUtilimport java.io.FileNotFoundExceptionimport java.io.FileInputStreamimport com.google.api.client.googleapis.auth.oauth2.GoogleClientSecretsimport java.io.InputStreamReaderimport com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlowimport com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledAppimport com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiverimport com.google.api.client.googleapis.javanet.GoogleNetHttpTransportimport kotlin.jvm.JvmStaticimport io.drive.DrvieInitimport com.google.api.client.http.javanet.NetHttpTransportimport com.google.api.services.drive.model.FileListimport java.io.BufferedInputStreamimport java.io.FileOutputStreamimport com.google.api.client.googleapis.media.MediaHttpDownloaderimport io.WebFileIOimport io.BCJSONimport page.LoadPageimport org.json.JSONObjectimport org.json.JSONArrayimport main.MainBCUimport main.Optsimport common.CommonStaticimport java.util.TreeMapimport java.util.Arraysimport java.io.BufferedReaderimport io.BCMusicimport common.util.stage.Musicimport io.BCPlayerimport java.util.HashMapimport javax.sound.sampled.Clipimport java.io.ByteArrayInputStreamimport javax.sound.sampled.AudioInputStreamimport javax.sound.sampled.AudioSystemimport javax.sound.sampled.DataLineimport javax.sound.sampled.FloatControlimport javax.sound.sampled.LineEventimport com.google.api.client.googleapis.media.MediaHttpDownloaderProgressListenerimport com.google.api.client.googleapis.media.MediaHttpDownloader.DownloadStateimport common.io.DataIOimport io.BCUReaderimport common.io.InStreamimport com.google.gson.JsonElementimport common.io.json.JsonDecoderimport com.google.gson.JsonObjectimport page.MainFrameimport page.view.ViewBox.Confimport page.MainLocaleimport page.battle.BattleInfoPageimport page.support.Exporterimport page.support.Importerimport common.pack.Context.ErrTypeimport common.util.stage.MapColcimport common.util.stage.MapColc.DefMapColcimport common.util.lang.MultiLangContimport common.util.stage.StageMapimport common.util.unit.Enemyimport io.BCUWriterimport java.text.SimpleDateFormatimport java.io.PrintStreamimport common.io.OutStreamimport common.battle.BasisSetimport res.AnimatedGifEncoderimport java.awt.image.BufferedImageimport javax.imageio.ImageIOimport java.security.MessageDigestimport java.security.NoSuchAlgorithmExceptionimport common.io.json.JsonEncoderimport java.io.FileWriterimport com.google.api.client.http.GenericUrlimport org.apache.http.impl .client.CloseableHttpClientimport org.apache.http.impl .client.HttpClientsimport org.apache.http.client.methods.HttpPostimport org.apache.http.entity.mime.content.FileBodyimport org.apache.http.entity.mime.MultipartEntityBuilderimport org.apache.http.entity.mime.HttpMultipartModeimport org.apache.http.HttpEntityimport org.apache.http.util.EntityUtilsimport com.google.api.client.http.HttpRequestInitializerimport com.google.api.client.http.HttpBackOffUnsuccessfulResponseHandlerimport com.google.api.client.util.ExponentialBackOffimport com.google.api.client.http.HttpBackOffIOExceptionHandlerimport res.NeuQuantimport res.LZWEncoderimport java.io.BufferedOutputStreamimport java.awt.Graphics2Dimport java.awt.image.DataBufferByteimport common.system.fake.FakeImageimport utilpc.awt.FIBIimport jogl.util.AmbImageimport common.system.files.VFileimport jogl.util.GLImageimport com.jogamp.opengl.util.texture.TextureDataimport common.system.Pimport com.jogamp.opengl.util.texture.TextureIOimport jogl.GLStaticimport com.jogamp.opengl.util.texture.awt.AWTTextureIOimport java.awt.AlphaCompositeimport common.system.fake.FakeImage.Markerimport jogl.util.GLGraphicsimport com.jogamp.opengl.GL2import jogl.util.GeoAutoimport com.jogamp.opengl.GL2ES3import com.jogamp.opengl.GLimport common.system.fake.FakeGraphicsimport common.system.fake.FakeTransformimport jogl.util.ResManagerimport jogl.util.GLGraphics.GeomGimport jogl.util.GLGraphics.GLCimport jogl.util.GLGraphics.GLTimport com.jogamp.opengl.GL2ES2import com.jogamp.opengl.util.glsl.ShaderCodeimport com.jogamp.opengl.util.glsl.ShaderProgramimport com.jogamp.opengl.GLExceptionimport jogl.StdGLCimport jogl.Tempimport common.util.anim.AnimUimport common.util.anim.EAnimUimport jogl.util.GLIBimport javax.swing.JFrameimport common.util.anim.AnimCEimport common.util.anim.AnimU.UTypeimport com.jogamp.opengl.util.FPSAnimatorimport com.jogamp.opengl.GLEventListenerimport com.jogamp.opengl.GLAutoDrawableimport page.awt.BBBuilderimport page.battle.BattleBox.OuterBoximport common.battle.SBCtrlimport page.battle.BattleBoximport jogl.GLBattleBoximport common.battle.BattleFieldimport page.anim.IconBoximport jogl.GLIconBoximport jogl.GLBBRecdimport page.awt.RecdThreadimport page.view.ViewBoximport jogl.GLViewBoximport page.view.ViewBox.Controllerimport java.awt.AWTExceptionimport page.battle.BBRecdimport jogl.GLRecorderimport com.jogamp.opengl.GLProfileimport com.jogamp.opengl.GLCapabilitiesimport page.anim.IconBox.IBCtrlimport page.anim.IconBox.IBConfimport page.view.ViewBox.VBExporterimport jogl.GLRecdBImgimport page.JTGimport jogl.GLCstdimport jogl.GLVBExporterimport common.util.anim.EAnimIimport page.RetFuncimport page.battle.BattleBox.BBPainterimport page.battle.BBCtrlimport javax.swing.JOptionPaneimport kotlin.jvm.Strictfpimport main.Invimport javax.swing.SwingUtilitiesimport java.lang.InterruptedExceptionimport utilpc.UtilPC.PCItrimport utilpc.awt.PCIBimport jogl.GLBBBimport page.awt.AWTBBBimport utilpc.Themeimport page.MainPageimport common.io.assets.AssetLoaderimport common.pack.Source.Workspaceimport common.io.PackLoader.ZipDesc.FileDescimport common.io.assets.Adminimport page.awt.BattleBoxDefimport page.awt.IconBoxDefimport page.awt.BBRecdAWTimport page.awt.ViewBoxDefimport org.jcodec.api.awt.AWTSequenceEncoderimport page.awt.RecdThread.PNGThreadimport page.awt.RecdThread.MP4Threadimport page.awt.RecdThread.GIFThreadimport java.awt.GradientPaintimport utilpc.awt.FG2Dimport page.anim.TreeContimport javax.swing.JTreeimport javax.swing.event.TreeExpansionListenerimport common.util.anim.MaModelimport javax.swing.tree.DefaultMutableTreeNodeimport javax.swing.event.TreeExpansionEventimport java.util.function.IntPredicateimport javax.swing.tree.DefaultTreeModelimport common.util.anim.EAnimDimport page.anim.AnimBoximport utilpc.PPimport common.CommonStatic.BCAuxAssetsimport common.CommonStatic.EditLinkimport page.JBTNimport page.anim.DIYViewPageimport page.anim.ImgCutEditPageimport page.anim.MaModelEditPageimport page.anim.MaAnimEditPageimport page.anim.EditHeadimport java.awt.event.ActionListenerimport page.anim.AbEditPageimport common.util.anim.EAnimSimport page.anim.ModelBoximport common.util.anim.ImgCutimport page.view.AbViewPageimport javax.swing.JListimport javax.swing.JScrollPaneimport javax.swing.JComboBoximport utilpc.UtilPCimport javax.swing.event.ListSelectionListenerimport javax.swing.event.ListSelectionEventimport common.system.VImgimport page.support.AnimLCRimport page.support.AnimTableimport common.util.anim.MaAnimimport java.util.EventObjectimport javax.swing.text.JTextComponentimport page.anim.PartEditTableimport javax.swing.ListSelectionModelimport page.support.AnimTableTHimport page.JTFimport utilpc.ReColorimport page.anim.ImgCutEditTableimport page.anim.SpriteBoximport page.anim.SpriteEditPageimport java.awt.event.FocusAdapterimport java.awt.event.FocusEventimport common.pack.PackData.UserPackimport utilpc.Algorithm.SRResultimport page.anim.MaAnimEditTableimport javax.swing.JSliderimport java.awt.event.MouseWheelEventimport common.util.anim.EPartimport javax.swing.event.ChangeEventimport page.anim.AdvAnimEditPageimport javax.swing.BorderFactoryimport page.JLimport javax.swing.ImageIconimport page.anim.MMTreeimport javax.swing.event.TreeSelectionListenerimport javax.swing.event.TreeSelectionEventimport page.support.AbJTableimport page.anim.MaModelEditTableimport page.info.edit.ProcTableimport page.info.edit.ProcTable.AtkProcTableimport page.info.edit.SwingEditorimport page.info.edit.ProcTable.MainProcTableimport page.support.ListJtfPolicyimport page.info.edit.SwingEditor.SwingEGimport common.util.Data.Procimport java.lang.Runnableimport javax.swing.JComponentimport page.info.edit.LimitTableimport page.pack.CharaGroupPageimport page.pack.LvRestrictPageimport javax.swing.SwingConstantsimport common.util.lang.Editors.EditorGroupimport common.util.lang.Editors.EdiFieldimport common.util.lang.Editorsimport common.util.lang.ProcLangimport page.info.edit.EntityEditPageimport common.util.lang.Editors.EditorSupplierimport common.util.lang.Editors.EditControlimport page.info.edit.SwingEditor.IntEditorimport page.info.edit.SwingEditor.BoolEditorimport page.info.edit.SwingEditor.IdEditorimport page.SupPageimport common.util.unit.AbEnemyimport common.pack.IndexContainer.Indexableimport common.pack.Context.SupExcimport common.battle.data .AtkDataModelimport utilpc.Interpretimport common.battle.data .CustomEntityimport page.info.filter.UnitEditBoximport common.battle.data .CustomUnitimport common.util.stage.SCGroupimport page.info.edit.SCGroupEditTableimport common.util.stage.SCDefimport page.info.filter.EnemyEditBoximport common.battle.data .CustomEnemyimport page.info.StageFilterPageimport page.view.BGViewPageimport page.view.CastleViewPageimport page.view.MusicPageimport common.util.stage.CastleImgimport common.util.stage.CastleListimport java.text.DecimalFormatimport common.util.stage.Recdimport common.util.stage.MapColc.PackMapColcimport page.info.edit.StageEditTableimport page.support.ReorderListimport page.info.edit.HeadEditTableimport page.info.filter.EnemyFindPageimport page.battle.BattleSetupPageimport page.info.edit.AdvStEditPageimport page.battle.StRecdPageimport page.info.edit.LimitEditPageimport page.support.ReorderListenerimport common.util.pack.Soulimport page.info.edit.AtkEditTableimport page.info.filter.UnitFindPageimport common.battle.Basisimport common.util.Data.Proc.IMUimport javax.swing.DefaultComboBoxModelimport common.util.Animableimport common.util.pack.Soul.SoulTypeimport page.view.UnitViewPageimport page.view.EnemyViewPageimport page.info.edit.SwingEditor.EditCtrlimport page.support.Reorderableimport page.info.EnemyInfoPageimport common.util.unit.EneRandimport page.pack.EREditPageimport page.support.InTableTHimport page.support.EnemyTCRimport javax.swing.DefaultListCellRendererimport page.info.filter.UnitListTableimport page.info.filter.UnitFilterBoximport page.info.filter.EnemyListTableimport page.info.filter.EnemyFilterBoximport page.info.filter.UFBButtonimport page.info.filter.UFBListimport common.battle.data .MaskUnitimport javax.swing.AbstractButtonimport page.support.SortTableimport page.info.UnitInfoPageimport page.support.UnitTCRimport page.info.filter.EFBButtonimport page.info.filter.EFBListimport common.util.stage.LvRestrictimport common.util.stage.CharaGroupimport page.info.StageTableimport page.info.TreaTableimport javax.swing.JPanelimport page.info.UnitInfoTableimport page.basis.BasisPageimport kotlin.jvm.JvmOverloadsimport page.info.EnemyInfoTableimport common.util.stage.RandStageimport page.info.StagePageimport page.info.StageRandPageimport common.util.unit.EFormimport page.pack.EREditTableimport common.util.EREntimport common.pack.FixIndexListimport page.support.UnitLCRimport page.pack.RecdPackPageimport page.pack.CastleEditPageimport page.pack.BGEditPageimport page.pack.CGLREditPageimport common.pack.Source.ZipSourceimport page.info.edit.EnemyEditPageimport page.info.edit.StageEditPageimport page.info.StageViewPageimport page.pack.UnitManagePageimport page.pack.MusicEditPageimport page.battle.AbRecdPageimport common.system.files.VFileRootimport java.awt.Desktopimport common.pack.PackDataimport common.util.unit.UnitLevelimport page.info.edit.FormEditPageimport common.util.anim.AnimIimport common.util.anim.AnimI.AnimTypeimport common.util.anim.AnimDimport common.battle.data .Orbimport page.basis.LineUpBoximport page.basis.LubContimport common.battle.BasisLUimport page.basis.ComboListTableimport page.basis.ComboListimport page.basis.NyCasBoximport page.basis.UnitFLUPageimport common.util.unit.Comboimport page.basis.LevelEditPageimport common.util.pack.NyCastleimport common.battle.LineUpimport common.system.SymCoordimport java.util.TreeSetimport page.basis.OrbBoximport javax.swing.table.DefaultTableCellRendererimport javax.swing.JTableimport common.CommonStatic.BattleConstimport common.battle.StageBasisimport common.util.ImgCoreimport common.battle.attack.ContAbimport common.battle.entity.EAnimContimport common.battle.entity.WaprContimport page.battle.RecdManagePageimport page.battle.ComingTableimport common.util.stage.EStageimport page.battle.EntityTableimport common.battle.data .MaskEnemyimport common.battle.SBRplyimport common.battle.entity.AbEntityimport page.battle.RecdSavePageimport page.LocCompimport page.LocSubCompimport javax.swing.table.TableModelimport page.support.TModelimport javax.swing.event.TableModelListenerimport javax.swing.table.DefaultTableColumnModelimport javax.swing.JFileChooserimport javax.swing.filechooser.FileNameExtensionFilterimport javax.swing.TransferHandlerimport java.awt.datatransfer.Transferableimport java.awt.datatransfer.DataFlavorimport javax.swing.DropModeimport javax.swing.TransferHandler.TransferSupportimport java.awt.dnd.DragSourceimport java.awt.datatransfer.UnsupportedFlavorExceptionimport common.system.Copableimport page.support.AnimTransferimport javax.swing.DefaultListModelimport page.support.InListTHimport java.awt.FocusTraversalPolicyimport javax.swing.JTextFieldimport page.CustomCompimport javax.swing.JToggleButtonimport javax.swing.JButtonimport javax.swing.ToolTipManagerimport javax.swing.JRootPaneimport javax.swing.JProgressBarimport page.ConfigPageimport page.view.EffectViewPageimport page.pack.PackEditPageimport page.pack.ResourcePageimport javax.swing.WindowConstantsimport java.awt.event.AWTEventListenerimport java.awt.AWTEventimport java.awt.event.ComponentAdapterimport java.awt.event.ComponentEventimport java.util.ConcurrentModificationExceptionimport javax.swing.plaf.FontUIResourceimport java.util.Enumerationimport javax.swing.UIManagerimport common.CommonStatic.FakeKeyimport page.LocSubComp.LocBinderimport page.LSCPopimport java.awt.BorderLayoutimport java.awt.GridLayoutimport javax.swing.JTextPaneimport page.TTTimport java.util.ResourceBundleimport java.util.MissingResourceExceptionimport java.util.Localeimport common.io.json.Test.JsonTest_2import common.pack.PackData.PackDescimport common.io.PackLoaderimport common.io.PackLoader.Preloadimport common.io.PackLoader.ZipDescimport common.io.json.Testimport common.io.json.JsonClassimport common.io.json.JsonFieldimport common.io.json.JsonField.GenTypeimport common.io.json.Test.JsonTest_0.JsonDimport common.io.json.JsonClass.RTypeimport java.util.HashSetimport common.io.json.JsonDecoder.OnInjectedimport common.io.json.JsonField.IOTypeimport common.io.json.JsonExceptionimport common.io.json.JsonClass.NoTagimport common.io.json.JsonField.SerTypeimport common.io.json.JsonClass.WTypeimport kotlin.reflect.KClassimport com.google.gson.JsonArrayimport common.io.assets.Admin.StaticPermittedimport common.io.json.JsonClass.JCGenericimport common.io.json.JsonClass.JCGetterimport com.google.gson.JsonPrimitiveimport com.google.gson.JsonNullimport common.io.json.JsonClass.JCIdentifierimport java.lang.ClassNotFoundExceptionimport common.io.assets.AssetLoader.AssetHeaderimport common.io.assets.AssetLoader.AssetHeader.AssetEntryimport common.io.InStreamDefimport common.io.BCUExceptionimport java.io.UnsupportedEncodingExceptionimport common.io.OutStreamDefimport javax.crypto.Cipherimport javax.crypto.spec.IvParameterSpecimport javax.crypto.spec.SecretKeySpecimport common.io.PackLoader.FileSaverimport common.system.files.FDByteimport common.io.json.JsonClass.JCConstructorimport common.io.PackLoader.FileLoader.FLStreamimport common.io.PackLoader.PatchFileimport java.lang.NullPointerExceptionimport java.lang.IndexOutOfBoundsExceptionimport common.io.MultiStreamimport java.io.RandomAccessFileimport common.io.MultiStream.TrueStreamimport java.lang.RuntimeExceptionimport common.pack.Source.ResourceLocationimport common.pack.Source.AnimLoaderimport common.pack.Source.SourceAnimLoaderimport common.util.anim.AnimCIimport common.system.files.FDFileimport common.pack.IndexContainerimport common.battle.data .PCoinimport common.util.pack.EffAnimimport common.battle.data .DataEnemyimport common.util.stage.Limit.DefLimitimport common.pack.IndexContainer.Reductorimport common.pack.FixIndexList.FixIndexMapimport common.pack.VerFixer.IdFixerimport common.pack.IndexContainer.IndexContimport common.pack.IndexContainer.ContGetterimport common.util.stage.CastleList.PackCasListimport common.util.Data.Proc.THEMEimport common.CommonStatic.ImgReaderimport common.pack.VerFixerimport common.pack.VerFixer.VerFixerExceptionimport java.lang.NumberFormatExceptionimport common.pack.Source.SourceAnimSaverimport common.pack.VerFixer.EnemyFixerimport common.pack.VerFixer.PackFixerimport common.pack.PackData.DefPackimport java.util.function.BiConsumerimport common.util.BattleStaticimport common.util.anim.AnimU.ImageKeeperimport common.util.anim.AnimCE.AnimCELoaderimport common.util.anim.AnimCI.AnimCIKeeperimport common.util.anim.AnimUD.DefImgLoaderimport common.util.BattleObjimport common.util.Data.Proc.ProcItemimport common.util.lang.ProcLang.ItemLangimport common.util.lang.LocaleCenter.Displayableimport common.util.lang.Editors.DispItemimport common.util.lang.LocaleCenter.ObjBinderimport common.util.lang.LocaleCenter.ObjBinder.BinderFuncimport common.util.Data.Proc.PROBimport org.jcodec.common.tools.MathUtilimport common.util.Data.Proc.PTimport common.util.Data.Proc.PTDimport common.util.Data.Proc.PMimport common.util.Data.Proc.WAVEimport common.util.Data.Proc.WEAKimport common.util.Data.Proc.STRONGimport common.util.Data.Proc.BURROWimport common.util.Data.Proc.REVIVEimport common.util.Data.Proc.SUMMONimport common.util.Data.Proc.MOVEWAVEimport common.util.Data.Proc.POISONimport common.util.Data.Proc.CRITIimport common.util.Data.Proc.VOLCimport common.util.Data.Proc.ARMORimport common.util.Data.Proc.SPEEDimport java.util.LinkedHashMapimport common.util.lang.LocaleCenter.DisplayItemimport common.util.lang.ProcLang.ProcLangStoreimport common.util.lang.Formatter.IntExpimport common.util.lang.Formatter.RefObjimport common.util.lang.Formatter.BoolExpimport common.util.lang.Formatter.BoolElemimport common.util.lang.Formatter.IElemimport common.util.lang.Formatter.Contimport common.util.lang.Formatter.Elemimport common.util.lang.Formatter.RefElemimport common.util.lang.Formatter.RefFieldimport common.util.lang.Formatter.RefFuncimport common.util.lang.Formatter.TextRefimport common.util.lang.Formatter.CodeBlockimport common.util.lang.Formatter.TextPlainimport common.util.unit.Unit.UnitInfoimport common.util.lang.MultiLangCont.MultiLangStaticsimport common.util.pack.EffAnim.EffTypeimport common.util.pack.EffAnim.ArmorEffimport common.util.pack.EffAnim.BarEneEffimport common.util.pack.EffAnim.BarrierEffimport common.util.pack.EffAnim.DefEffimport common.util.pack.EffAnim.WarpEffimport common.util.pack.EffAnim.ZombieEffimport common.util.pack.EffAnim.KBEffimport common.util.pack.EffAnim.SniperEffimport common.util.pack.EffAnim.VolcEffimport common.util.pack.EffAnim.SpeedEffimport common.util.pack.EffAnim.WeakUpEffimport common.util.pack.EffAnim.EffAnimStoreimport common.util.pack.NyCastle.NyTypeimport common.util.pack.WaveAnimimport common.util.pack.WaveAnim.WaveTypeimport common.util.pack.Background.BGWvTypeimport common.util.unit.Form.FormJsonimport common.system.BasedCopableimport common.util.anim.AnimUDimport common.battle.data .DataUnitimport common.battle.entity.EUnitimport common.battle.entity.EEnemyimport common.util.EntRandimport common.util.stage.Recd.Waitimport java.lang.CloneNotSupportedExceptionimport common.util.stage.StageMap.StageMapInfoimport common.util.stage.Stage.StageInfoimport common.util.stage.Limit.PackLimitimport common.util.stage.MapColc.ClipMapColcimport common.util.stage.CastleList.DefCasListimport common.util.stage.MapColc.StItrimport common.util.Data.Proc.IntType.BitCountimport common.util.CopRandimport common.util.LockGLimport java.lang.IllegalAccessExceptionimport common.battle.data .MaskAtkimport common.battle.data .DefaultDataimport common.battle.data .DataAtkimport common.battle.data .MaskEntityimport common.battle.data .DataEntityimport common.battle.attack.AtkModelAbimport common.battle.attack.AttackAbimport common.battle.attack.AttackSimpleimport common.battle.attack.AttackWaveimport common.battle.entity.Cannonimport common.battle.attack.AttackVolcanoimport common.battle.attack.ContWaveAbimport common.battle.attack.ContWaveDefimport common.battle.attack.AtkModelEntityimport common.battle.entity.EntContimport common.battle.attack.ContMoveimport common.battle.attack.ContVolcanoimport common.battle.attack.ContWaveCanonimport common.battle.attack.AtkModelEnemyimport common.battle.attack.AtkModelUnitimport common.battle.attack.AttackCanonimport common.battle.entity.EUnit.OrbHandlerimport common.battle.entity.Entity.AnimManagerimport common.battle.entity.Entity.AtkManagerimport common.battle.entity.Entity.ZombXimport common.battle.entity.Entity.KBManagerimport common.battle.entity.Entity.PoisonTokenimport common.battle.entity.Entity.WeakTokenimport common.battle.Treasureimport common.battle.MirrorSetimport common.battle.Releaseimport common.battle.ELineUpimport common.battle.entity.Sniperimport common.battle.entity.ECastleimport java.util.Dequeimport common.CommonStatic.Itfimport java.lang.Characterimport common.CommonStatic.ImgWriterimport utilpc.awt.FTATimport utilpc.awt.Blenderimport java.awt.RenderingHintsimport utilpc.awt.BIBuilderimport java.awt.CompositeContextimport java.awt.image.Rasterimport java.awt.image.WritableRasterimport utilpc.ColorSetimport utilpc.OggTimeReaderimport utilpc.UtilPC.PCItr.MusicReaderimport utilpc.UtilPC.PCItr.PCALimport javax.swing.UIManager.LookAndFeelInfoimport java.lang.InstantiationExceptionimport javax.swing.UnsupportedLookAndFeelExceptionimport utilpc.Algorithm.ColorShiftimport utilpc.Algorithm.StackRect
object Res : ImgCore {
    fun getBase(ae: AbEntity, coor: SymCoord): P {
        val aux: BCAuxAssets = CommonStatic.getBCAssets()
        var h: Long = ae.health
        if (h < 0) h = 0
        val val0 = Res.getLab(h)
        val val1 = Res.getLab(ae.maxH)
        val input: Array<FakeImage?> = arrayOfNulls<FakeImage>(val0.size + val1.size + 1)
        for (i in val0.indices) input[i] = aux.num.get(5).get(val0[i]).getImg()
        input[val0.size] = aux.num.get(5).get(10).getImg()
        for (i in val1.indices) input[val0.size + i + 1] = aux.num.get(5).get(val1[i]).getImg()
        return coor.draw(*input)
    }

    fun getCost(cost: Int, enable: Boolean, coor: SymCoord): P {
        val aux: BCAuxAssets = CommonStatic.getBCAssets()
        if (cost == -1) return coor.draw(aux.battle.get(0).get(3).getImg())
        val `val` = Res.getLab(cost.toLong())
        val input: Array<FakeImage?> = arrayOfNulls<FakeImage>(`val`.size)
        for (i in `val`.indices) input[i] = aux.num.get(if (enable) 3 else 4).get(`val`[i]).getImg()
        return coor.draw(*input)
    }

    fun getMoney(mon: Int, max: Int, coor: SymCoord): P {
        val aux: BCAuxAssets = CommonStatic.getBCAssets()
        val val0 = Res.getLab(mon.toLong())
        val val1 = Res.getLab(max.toLong())
        val input: Array<FakeImage?> = arrayOfNulls<FakeImage>(val0.size + val1.size + 1)
        for (i in val0.indices) input[i] = aux.num.get(0).get(val0[i]).getImg()
        input[val0.size] = aux.num.get(0).get(10).getImg()
        for (i in val1.indices) input[val0.size + i + 1] = aux.num.get(0).get(val1[i]).getImg()
        return coor.draw(*input)
    }

    fun getWorkerLv(lv: Int, enable: Boolean, coor: SymCoord): P {
        val aux: BCAuxAssets = CommonStatic.getBCAssets()
        return coor.draw(aux.num.get(if (enable) 1 else 2).get(10).getImg(), aux.num.get(if (enable) 1 else 2).get(lv).getImg())
    }

    fun readData() {
        val aux: BCAuxAssets = CommonStatic.getBCAssets()
        aux.unicut = ImgCut.Companion.newIns("./org/data/uni.imgcut")
        aux.udicut = ImgCut.Companion.newIns("./org/data/udi.imgcut")
        val uni = VImg("./org/page/uni.png")
        uni.setCut(aux.unicut)
        aux.slot.get(0) = uni
        aux.ico.get(0) = arrayOfNulls<VImg>(6)
        aux.ico.get(1) = arrayOfNulls<VImg>(4)
        aux.ico.get(0).get(0) = VImg("./org/page/foreground.png")
        aux.ico.get(0).get(1) = VImg("./org/page/starFG.png")
        aux.ico.get(0).get(2) = VImg("./org/page/EFBG.png")
        aux.ico.get(0).get(3) = VImg("./org/page/TFBG.png")
        aux.ico.get(0).get(4) = VImg("./org/page/glow.png")
        aux.ico.get(0).get(5) = VImg("./org/page/EFFG.png")
        aux.ico.get(1).get(0) = VImg("./org/page/uni_f.png")
        aux.ico.get(1).get(1) = VImg("./org/page/uni_c.png")
        aux.ico.get(1).get(2) = VImg("./org/page/uni_s.png")
        aux.ico.get(1).get(3) = VImg("./org/page/uni_box.png")
        for (vs in aux.ico.get(1)) vs.setCut(aux.unicut)
        val ic029: ImgCut = ImgCut.Companion.newIns("./org/page/img029.imgcut")
        val img029 = VImg("./org/page/img029.png")
        val parts: Array<FakeImage> = ic029.cut(img029.getImg())
        aux.slot.get(1) = VImg(parts[9])
        aux.slot.get(2) = VImg(parts[10])
        Res.readAbiIcon()
        Res.readBattle()
    }

    private fun getLab(cost: Long): IntArray {
        var cost = cost
        if (cost < 0) cost = 0
        var len = Math.log10(if (cost == 0L) 1 else cost.toDouble()).toInt() + 1
        if (len < 0) len = 0
        val input = IntArray(len)
        for (i in 0 until len) {
            input[len - i - 1] = (cost % 10).toInt()
            cost /= 10
        }
        return input
    }

    private fun readAbiIcon() {
        val aux: BCAuxAssets = CommonStatic.getBCAssets()
        CommonStatic.getConfig().icon = true
        val ic015: ImgCut = ImgCut.Companion.newIns("./org/page/img015.imgcut")
        val img015 = VImg("./org/page/img015.png")
        val parts: Array<FakeImage> = ic015.cut(img015.getImg())
        aux.icon.get(0) = arrayOfNulls<VImg>(Data.Companion.ABI_TOT)
        aux.icon.get(1) = arrayOfNulls<VImg>(Data.Companion.PROC_TOT)
        aux.icon.get(2) = arrayOfNulls<VImg>(Data.Companion.ATK_TOT)
        aux.icon.get(3) = arrayOfNulls<VImg>(Data.Companion.TRAIT_TOT)
        aux.icon.get(3).get(Data.Companion.TRAIT_RED) = VImg(parts[77])
        aux.icon.get(3).get(Data.Companion.TRAIT_FLOAT) = VImg(parts[78])
        aux.icon.get(3).get(Data.Companion.TRAIT_BLACK) = VImg(parts[79])
        aux.icon.get(3).get(Data.Companion.TRAIT_METAL) = VImg(parts[80])
        aux.icon.get(3).get(Data.Companion.TRAIT_ANGEL) = VImg(parts[81])
        aux.icon.get(3).get(Data.Companion.TRAIT_ALIEN) = VImg(parts[82])
        aux.icon.get(3).get(Data.Companion.TRAIT_ZOMBIE) = VImg(parts[83])
        aux.icon.get(3).get(Data.Companion.TRAIT_RELIC) = VImg(parts[84])
        aux.icon.get(0).get(Data.Companion.ABI_EKILL) = VImg(parts[110])
        aux.icon.get(2).get(Data.Companion.ATK_OMNI) = VImg(parts[112])
        aux.icon.get(1).get(Data.Companion.P_IMUCURSE) = VImg(parts[116])
        aux.icon.get(1).get(Data.Companion.P_WEAK) = VImg(parts[195])
        aux.icon.get(1).get(Data.Companion.P_STRONG) = VImg(parts[196])
        aux.icon.get(1).get(Data.Companion.P_STOP) = VImg(parts[197])
        aux.icon.get(1).get(Data.Companion.P_SLOW) = VImg(parts[198])
        aux.icon.get(1).get(Data.Companion.P_LETHAL) = VImg(parts[199])
        aux.icon.get(0).get(Data.Companion.ABI_BASE) = VImg(parts[200])
        aux.icon.get(1).get(Data.Companion.P_CRIT) = VImg(parts[201])
        aux.icon.get(0).get(Data.Companion.ABI_ONLY) = VImg(parts[202])
        aux.icon.get(0).get(Data.Companion.ABI_GOOD) = VImg(parts[203])
        aux.icon.get(0).get(Data.Companion.ABI_RESIST) = VImg(parts[204])
        aux.icon.get(0).get(Data.Companion.ABI_EARN) = VImg(parts[205])
        aux.icon.get(0).get(Data.Companion.ABI_MASSIVE) = VImg(parts[206])
        aux.icon.get(1).get(Data.Companion.P_KB) = VImg(parts[207])
        aux.icon.get(1).get(Data.Companion.P_WAVE) = VImg(parts[208])
        aux.icon.get(0).get(Data.Companion.ABI_METALIC) = VImg(parts[209])
        aux.icon.get(1).get(Data.Companion.P_IMUWAVE) = VImg(parts[210])
        aux.icon.get(2).get(Data.Companion.ATK_AREA) = VImg(parts[211])
        aux.icon.get(2).get(Data.Companion.ATK_LD) = VImg(parts[212])
        aux.icon.get(1).get(Data.Companion.P_IMUWEAK) = VImg(parts[213])
        aux.icon.get(1).get(Data.Companion.P_IMUSTOP) = VImg(parts[214])
        aux.icon.get(1).get(Data.Companion.P_IMUSLOW) = VImg(parts[215])
        aux.icon.get(1).get(Data.Companion.P_IMUKB) = VImg(parts[216])
        aux.icon.get(2).get(Data.Companion.ATK_SINGLE) = VImg(parts[217])
        aux.icon.get(0).get(Data.Companion.ABI_WAVES) = VImg(parts[218])
        aux.icon.get(0).get(Data.Companion.ABI_WKILL) = VImg(parts[258])
        aux.icon.get(0).get(Data.Companion.ABI_RESISTS) = VImg(parts[122])
        aux.icon.get(0).get(Data.Companion.ABI_MASSIVES) = VImg(parts[114])
        aux.icon.get(0).get(Data.Companion.ABI_ZKILL) = VImg(parts[260])
        aux.icon.get(1).get(Data.Companion.P_IMUWARP) = VImg(parts[262])
        aux.icon.get(1).get(Data.Companion.P_BREAK) = VImg(parts[264])
        aux.icon.get(1).get(Data.Companion.P_WARP) = VImg(parts[266])
        aux.icon.get(1).get(Data.Companion.P_SATK) = VImg(parts[229])
        aux.icon.get(1).get(Data.Companion.P_IMUATK) = VImg(parts[231])
        aux.icon.get(1).get(Data.Companion.P_VOLC) = VImg(parts[239])
        aux.icon.get(1).get(Data.Companion.P_IMUPOIATK) = VImg(parts[237])
        aux.icon.get(1).get(Data.Companion.P_IMUVOLC) = VImg(parts[243])
        aux.icon.get(1).get(Data.Companion.P_CURSE) = VImg(parts[289])
        aux.icon.get(0).get(Data.Companion.ABI_THEMEI) = VImg("./org/page/icons/ThemeX.png")
        aux.icon.get(0).get(Data.Companion.ABI_TIMEI) = VImg("./org/page/icons/TimeX.png")
        aux.icon.get(0).get(Data.Companion.ABI_IMUSW) = VImg("./org/page/icons/BossWaveX.png")
        aux.icon.get(0).get(Data.Companion.ABI_SNIPERI) = VImg("./org/page/icons/SnipeX.png")
        aux.icon.get(0).get(Data.Companion.ABI_POII) = VImg("./org/page/icons/PoisonX.png")
        aux.icon.get(0).get(Data.Companion.ABI_SEALI) = VImg("./org/page/icons/SealX.png")
        aux.icon.get(0).get(Data.Companion.ABI_GHOST) = VImg("./org/page/icons/Ghost.png")
        aux.icon.get(1).get(Data.Companion.P_THEME) = VImg("./org/page/icons/Theme.png")
        aux.icon.get(1).get(Data.Companion.P_TIME) = VImg("./org/page/icons/Time.png")
        aux.icon.get(1).get(Data.Companion.P_BOSS) = VImg("./org/page/icons/BossWave.png")
        aux.icon.get(1).get(Data.Companion.P_SNIPER) = VImg("./org/page/icons/Snipe.png")
        aux.icon.get(1).get(Data.Companion.P_POISON) = VImg("./org/page/icons/Poison.png")
        aux.icon.get(1).get(Data.Companion.P_SEAL) = VImg("./org/page/icons/Seal.png")
        aux.icon.get(1).get(Data.Companion.P_MOVEWAVE) = VImg("./org/page/icons/Moving.png")
        aux.icon.get(1).get(Data.Companion.P_SUMMON) = VImg("./org/page/icons/Summon.png")
        aux.icon.get(0).get(Data.Companion.ABI_MOVEI) = VImg("./org/page/icons/MovingX.png")
        aux.icon.get(0).get(Data.Companion.ABI_GLASS) = VImg("./org/page/icons/Suicide.png")
        aux.icon.get(1).get(Data.Companion.P_BURROW) = VImg("./org/page/icons/Burrow.png")
        aux.icon.get(1).get(Data.Companion.P_REVIVE) = VImg("./org/page/icons/Revive.png")
        aux.icon.get(1).get(Data.Companion.P_CRITI) = VImg("./org/page/icons/CritX.png")
        aux.icon.get(3).get(Data.Companion.TRAIT_WHITE) = VImg("./org/page/icons/White.png")
        aux.icon.get(1).get(Data.Companion.P_POIATK) = VImg("./org/page/icons/BCPoison.png")
        aux.icon.get(1).get(Data.Companion.P_ARMOR) = VImg("./org/page/icons/ArmorBreak.png")
        aux.icon.get(1).get(Data.Companion.P_SPEED) = VImg("./org/page/icons/Speed.png")
        CommonStatic.getConfig().icon = false
    }

    private fun readBattle() {
        val aux: BCAuxAssets = CommonStatic.getBCAssets()
        aux.battle.get(0) = arrayOfNulls<VImg>(4)
        aux.battle.get(1) = arrayOfNulls<VImg>(12)
        aux.battle.get(2) = arrayOfNulls<VImg>(5)
        val ic001: ImgCut = ImgCut.Companion.newIns("./org/page/img001.imgcut")
        val img001 = VImg("./org/page/img001.png")
        var parts: Array<FakeImage?> = ic001.cut(img001.getImg())
        val vals = intArrayOf(5, 19, 30, 40, 51, 62, 73, 88, 115)
        val adds = intArrayOf(1, 2, 2, 0, 0, 1, 1, 1, 0)
        for (i in 0..8) {
            for (j in 0..9) aux.num.get(i).get(j) = VImg(parts[vals[i] - 5 + j])
            if (adds[i] == 1) aux.num.get(i).get(10) = VImg(parts[vals[i] + 5])
            if (adds[i] == 2) aux.num.get(i).get(10) = VImg(parts[vals[i] - 6])
        }
        aux.battle.get(0).get(3) = VImg(parts[81])
        val ic002: ImgCut = ImgCut.Companion.newIns("./org/page/img002.imgcut")
        val img002 = VImg("./org/page/img002.png")
        parts = ic002.cut(img002.getImg())
        aux.battle.get(0).get(0) = VImg(parts[5])
        aux.battle.get(0).get(1) = VImg(parts[24])
        aux.battle.get(0).get(2) = VImg(parts[6])
        aux.battle.get(1).get(0) = VImg(parts[8])
        aux.battle.get(1).get(1) = VImg(parts[7])
        for (i in 0..9) aux.battle.get(1).get(2 + i) = VImg(parts[11 + i])
        aux.battle.get(2).get(0) = VImg(parts[27])
        aux.battle.get(2).get(1) = VImg(parts[29])
        aux.battle.get(2).get(2) = VImg(parts[32])
        aux.battle.get(2).get(3) = VImg(parts[33])
        aux.battle.get(2).get(4) = VImg(parts[38])
        // money, lv, lv dark,cost,cost dark,hp, money light,time,point
    }
}