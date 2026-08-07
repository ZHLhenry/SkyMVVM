package com.sky.mvvm.sample.feature.mine.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.SpannableString
import android.view.View
import com.azhon.appupdate.listener.OnDownloadListenerAdapter
import com.sky.mvvm.core.common.base.BaseFragment
import com.sky.mvvm.ext.download.DownloadResultState
import com.sky.mvvm.ext.download.FileTool
import com.sky.mvvm.sample.R
import com.sky.mvvm.sample.databinding.FragmentMineBinding
import com.sky.mvvm.sample.feature.mine.vm.MineViewModel
import com.sky.mvvm.sample.feature.other.ui.LoginActivity
import com.sky.widget.iconfont.SkyIconFontsLib
import com.azhon.appupdate.manager.DownloadManager
import com.sky.mvvm.sample.download.SkyDownloadManager
import dagger.hilt.android.AndroidEntryPoint

/**
 * <p>{@code className: }</p>
 * <p>{@code author: Henry}</p>
 * <p>{@code date: 2025/3/4 20:27}</p>
 * <p>{@code description: 我的页面，包含下载示例}</p>
 */
@AndroidEntryPoint
class MineFragment : BaseFragment<MineViewModel, FragmentMineBinding>() {

    override fun initView(savedInstanceState: Bundle?) {
        addLoadingObserve(mViewModel)
        mDatabind.statelayout.showContent()
        mDatabind.btnLogin.setOnClickListener {
            startActivity(Intent(requireContext(), LoginActivity::class.java))
        }
        // 通过 SkyIconFontsLib.style() 解析 {prefix_name} 格式的图标标记
        mDatabind.tvIcon.text = SkyIconFontsLib.style(
            SpannableString("${SkyIconFontsLib.getFormattedIconName("skyshouye")} 首页")
        )

        initDownload()
        initAppUpdateDownload()
    }

    /**
     * 初始化下载功能示例
     */
    @SuppressLint("SetTextI18n")
    private fun initDownload() {
        // 观察下载状态
        mViewModel.downloadResultState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is DownloadResultState.Pending -> {
                    mDatabind.progressBar.visibility = View.VISIBLE
                    mDatabind.tvDownloadStatus.visibility = View.VISIBLE
                    mDatabind.tvDownloadStatus.text = "等待下载..."
                    mDatabind.btnDownload.text = "下载中"
                }
                is DownloadResultState.Progress -> {
                    mDatabind.progressBar.progress = state.progress
                    mDatabind.tvDownloadStatus.text =
                        "下载进度: ${state.progress}%  ${FileTool.bytes2kb(state.soFarBytes)} / ${FileTool.bytes2kb(state.totalBytes)}"
                }
                is DownloadResultState.Success -> {
                    mDatabind.progressBar.visibility = View.GONE
                    mDatabind.tvDownloadStatus.text = "下载完成: ${state.filePath}"
                    mDatabind.btnDownload.text = "重新下载"
                }
                is DownloadResultState.Pause -> {
                    mDatabind.tvDownloadStatus.text = "下载已暂停"
                    mDatabind.btnDownload.text = "继续下载"
                }
                is DownloadResultState.Error -> {
                    mDatabind.progressBar.visibility = View.GONE
                    mDatabind.tvDownloadStatus.text = "下载失败: ${state.errorMsg}"
                    mDatabind.btnDownload.text = "重新下载"
                }
            }
        }

        // 点击下载按钮，调用 ViewModel 方法
        mDatabind.btnDownload.setOnClickListener {
            mViewModel.startDownload(
                url = "https://cdn.jsdelivr.net/gh/bitbar/test-samples@master/apps/android/bitbar-sample-app.apk",
                saveName = "bitbar-sample-app.apk"
            )
        }
    }

    /**
     * 初始化 AppUpdate 下载功能示例
     * 使用 io.github.azhon:appupdate 库，点击按钮弹出更新提示对话框
     * 通过 SkyDownloadManager 使用 SkyMVVMLib 的 DownLoadManager 进行下载
     */
    private fun initAppUpdateDownload() {
        mDatabind.btnAppUpdateDownload.setOnClickListener {
            val manager = DownloadManager.Builder(requireActivity()).run {
                apkUrl("https://cdn.jsdelivr.net/gh/bitbar/test-samples@master/apps/android/bitbar-sample-app.apk")
                apkName("bitbar-sample-app-update.apk")
                smallIcon(R.mipmap.ic_launcher)
                showNewerToast(true)
                apkVersionCode(150)
                apkVersionName("v4.2.1")
                apkSize("7.7MB")
                apkDescription("yyyyyyyyyyyy")
                enableLog(true)
                jumpInstallPage(true)
                dialogButtonTextColor(Color.WHITE)
                showNotification(true)
                showBgdToast(false)
                forcedUpgrade(false)
                // 使用 SkyMVVMLib 的 DownLoadManager 作为自定义下载器
//                httpManager(SkyDownloadManager())
                build()
            }
            manager.download()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // 页面销毁时取消下载
        mViewModel.cancelDownload()
    }
}
