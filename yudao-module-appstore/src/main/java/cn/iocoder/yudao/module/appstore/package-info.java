/**
 * appstore 模块，提供安卓 TV 应用商店后台管理能力。
 * 包括：应用分类管理、应用信息管理、应用版本管理（含 APK 上传下载）。
 *
 * 1. Controller URL：以 /appstore/ 开头，避免和其它 Module 冲突
 * 2. DataObject 表名：以 appstore_ 开头，方便在数据库中区分
 */
package cn.iocoder.yudao.module.appstore;
