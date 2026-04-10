# AppStore 模块接口文档

## 1. 通用说明

### 1.1 基础路径

| 端 | 前缀 | 说明 |
|----|------|------|
| 管理后台 | `{host}/admin-api` | 需登录后台，并具备对应权限 |
| TV / 用户端 | `{host}/app-api` | 部分接口 `@PermitAll`，其余按全局安全配置 |

> 芋道框架按包名自动加前缀：`**.controller.admin.**` => `/admin-api`，`**.controller.app.**` => `/app-api`。

### 1.2 请求头

- 多租户场景需传 `tenant-id`（以项目网关/安全配置为准）。
- 管理后台鉴权：`Authorization: Bearer {accessToken}`。
- JSON 接口使用 `Content-Type: application/json`；上传接口使用 `multipart/form-data`。

### 1.3 统一响应体 `CommonResult<T>`

| 字段 | 类型 | 说明 |
|------|------|------|
| `code` | Integer | 业务码，成功一般为 `0` |
| `msg` | String | 提示信息 |
| `data` | T | 业务数据 |

**成功响应 JSON 结构（外层固定）：**

```json
{
  "code": 0,
  "msg": "",
  "data": null
}
```

> `data` 随接口变化；失败时 `code` 非 `0`，`msg` 为错误说明，`data` 常为 `null`。

**失败响应 JSON 示例（结构仍为 `CommonResult`，仅 `code`/`msg` 变化）：**

```json
{
  "code": 400,
  "msg": "应用名称不能为空",
  "data": null
}
```

### 1.4 分页结果 `PageResult<T>`

分页接口的 `data` 为该结构；`list` 为当前页数据数组，`total` 为符合条件的总条数。

```json
{
  "total": 100,
  "list": []
}
```

### 1.5 分页公共参数 `PageParam`

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| `pageNo` | Integer | 是 | 页码，从 1 开始 |
| `pageSize` | Integer | 是 | 每页条数，范围 1-200 |

### 1.6 日期时间字段

接口中的 `LocalDateTime` 在 JSON 中一般为 ISO-8601 字符串，例如 `"2024-06-01T14:30:00"`（具体格式以服务端 `Jackson` 配置为准）。

---

## 2. 管理后台 - 应用管理

- **Tag**：`管理后台 - 应用管理`
- **Base Path**：`/appstore/app`

| 方法 | 路径 | 权限 | 说明 |
|------|------|------|------|
| POST | `/create` | `appstore:app:create` | 创建应用 |
| PUT | `/update` | `appstore:app:update` | 更新应用 |
| PUT | `/update-status` | `appstore:app:update` | 更新状态（上架/下架） |
| DELETE | `/delete` | `appstore:app:delete` | 删除应用 |
| GET | `/get` | `appstore:app:query` | 获取应用详情 |
| GET | `/page` | `appstore:app:query` | 应用分页查询 |

### 2.1 `POST /create`、`PUT /update`

请求体：`AppSaveReqVO`

| 字段 | 类型 | 必填 | 说明 |
|------|------|------|------|
| `id` | Long | 否（更新时传） | 应用编号 |
| `categoryId` | Long | 是 | 所属分类编号 |
| `name` | String | 是 | 应用名称，最长 128 |
| `packageName` | String | 是 | 安卓包名，最长 256 |
| `iconUrl` | String | 否 | 图标 URL |
| `description` | String | 否 | 应用描述 |
| `screenshots` | String | 否 | 截图 URL JSON 数组字符串 |
| `developer` | String | 否 | 开发者名称 |

### 2.2 `PUT /update-status`

Query 参数：

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| `id` | Long | 是 | 应用编号 |
| `status` | Integer | 是 | `0` 草稿，`1` 已上架，`2` 已下架 |

### 2.3 `GET /page`

Query 参数：`AppPageReqVO + PageParam`

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| `name` | String | 否 | 应用名称，模糊匹配 |
| `packageName` | String | 否 | 包名，模糊匹配 |
| `categoryId` | Long | 否 | 分类编号 |
| `status` | Integer | 否 | 状态 |
| `pageNo` | Integer | 是 | 页码 |
| `pageSize` | Integer | 是 | 每页条数 |

### 2.4 响应 JSON 示例

**`POST /create`** — `data` 为新建应用主键：

```json
{
  "code": 0,
  "msg": "",
  "data": 1001
}
```

**`PUT /update`、`PUT /update-status`、`DELETE /delete`** — `data` 为是否成功：

```json
{
  "code": 0,
  "msg": "",
  "data": true
}
```

**`GET /get`** — `data` 为 `AppRespVO`（`latestVersionId` 等可能为 `null`）：

```json
{
  "code": 0,
  "msg": "",
  "data": {
    "id": 1001,
    "categoryId": 10,
    "name": "芒果TV",
    "packageName": "com.mgtv.tv",
    "iconUrl": "https://example.com/icon.png",
    "description": "一款热门的视频应用",
    "screenshots": "[\"https://example.com/s1.png\",\"https://example.com/s2.png\"]",
    "developer": "芒果TV",
    "status": 1,
    "downloadCount": 10000,
    "latestVersionId": 5001,
    "createTime": "2024-06-01T10:00:00"
  }
}
```

**`GET /page`** — `data` 为 `PageResult<AppRespVO>`：

```json
{
  "code": 0,
  "msg": "",
  "data": {
    "total": 1,
    "list": [
      {
        "id": 1001,
        "categoryId": 10,
        "name": "芒果TV",
        "packageName": "com.mgtv.tv",
        "iconUrl": "https://example.com/icon.png",
        "description": "一款热门的视频应用",
        "screenshots": "[\"https://example.com/s1.png\"]",
        "developer": "芒果TV",
        "status": 1,
        "downloadCount": 10000,
        "latestVersionId": 5001,
        "createTime": "2024-06-01T10:00:00"
      }
    ]
  }
}
```

---

## 3. 管理后台 - 应用分类

- **Tag**：`管理后台 - 应用分类`
- **Base Path**：`/appstore/category`

| 方法 | 路径 | 权限 | 说明 |
|------|------|------|------|
| POST | `/create` | `appstore:category:create` | 创建分类 |
| PUT | `/update` | `appstore:category:update` | 更新分类 |
| DELETE | `/delete` | `appstore:category:delete` | 删除分类 |
| GET | `/get` | `appstore:category:query` | 获取分类详情 |
| GET | `/page` | `appstore:category:query` | 分类分页查询 |
| GET | `/simple-list` | - | 分类精简列表（仅已启用） |

### 3.1 `POST /create`、`PUT /update`

请求体：`CategorySaveReqVO`

| 字段 | 类型 | 必填 | 说明 |
|------|------|------|------|
| `id` | Long | 否（更新时传） | 分类编号 |
| `name` | String | 是 | 分类名称，最长 64 |
| `iconUrl` | String | 否 | 分类图标 URL |
| `sort` | Integer | 是 | 排序权重 |
| `status` | Integer | 是 | 状态（`CommonStatusEnum`） |

### 3.2 `GET /page`

Query 参数：`CategoryPageReqVO + PageParam`

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| `name` | String | 否 | 分类名称，模糊匹配 |
| `status` | Integer | 否 | 状态 |
| `pageNo` | Integer | 是 | 页码 |
| `pageSize` | Integer | 是 | 每页条数 |

### 3.3 响应 JSON 示例

**`POST /create`** — `data` 为分类主键：

```json
{
  "code": 0,
  "msg": "",
  "data": 201
}
```

**`PUT /update`、`DELETE /delete`**：

```json
{
  "code": 0,
  "msg": "",
  "data": true
}
```

**`GET /get`** — `data` 为 `CategoryRespVO`：

```json
{
  "code": 0,
  "msg": "",
  "data": {
    "id": 201,
    "name": "影音娱乐",
    "iconUrl": "https://example.com/cat.png",
    "sort": 1,
    "status": 0,
    "createTime": "2024-06-01T09:00:00"
  }
}
```

**`GET /page`** — `data` 为 `PageResult<CategoryRespVO>`：

```json
{
  "code": 0,
  "msg": "",
  "data": {
    "total": 1,
    "list": [
      {
        "id": 201,
        "name": "影音娱乐",
        "iconUrl": "https://example.com/cat.png",
        "sort": 1,
        "status": 0,
        "createTime": "2024-06-01T09:00:00"
      }
    ]
  }
}
```

**`GET /simple-list`** — `data` 为 `CategorySimpleRespVO` 数组：

```json
{
  "code": 0,
  "msg": "",
  "data": [
    {
      "id": 201,
      "name": "影音娱乐"
    },
    {
      "id": 202,
      "name": "游戏"
    }
  ]
}
```

---

## 4. 管理后台 - 应用版本

- **Tag**：`管理后台 - 应用版本`
- **Base Path**：`/appstore/app-version`

| 方法 | 路径 | 权限 | 说明 |
|------|------|------|------|
| POST | `/upload-apk` | `appstore:app-version:create` | 上传 APK 并返回 URL |
| POST | `/create` | `appstore:app-version:create` | 创建应用版本 |
| PUT | `/update` | `appstore:app-version:update` | 更新应用版本 |
| DELETE | `/delete` | `appstore:app-version:delete` | 删除应用版本 |
| GET | `/get` | `appstore:app-version:query` | 获取版本详情 |
| GET | `/page` | `appstore:app-version:query` | 版本分页查询 |

### 4.1 `POST /upload-apk`

- 请求类型：`multipart/form-data`
- 表单字段：`file`（`MultipartFile`）
- 返回：`CommonResult<String>`（APK URL）

**`POST /upload-apk` 响应示例：**

```json
{
  "code": 0,
  "msg": "",
  "data": "https://file.example.com/appstore/apk/xxx.apk"
}
```

### 4.2 `POST /create`、`PUT /update`

请求体：`AppVersionSaveReqVO`

| 字段 | 类型 | 必填 | 说明 |
|------|------|------|------|
| `id` | Long | 否（更新时传） | 版本编号 |
| `appId` | Long | 是 | 关联应用编号 |
| `versionName` | String | 是 | 版本号（如 `1.2.0`） |
| `versionCode` | Integer | 是 | 安卓 versionCode |
| `apkUrl` | String | 是 | APK 下载地址 |
| `apkSize` | Long | 否 | APK 大小（字节） |
| `updateDescription` | String | 否 | 更新说明 |
| `status` | Integer | 否 | `0` 草稿，`1` 已发布 |

### 4.3 `GET /page`

Query 参数：`AppVersionPageReqVO + PageParam`

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| `appId` | Long | 是 | 关联应用编号 |
| `status` | Integer | 否 | 状态 |
| `pageNo` | Integer | 是 | 页码 |
| `pageSize` | Integer | 是 | 每页条数 |

### 4.4 响应 JSON 示例

**`POST /create`** — `data` 为版本主键：

```json
{
  "code": 0,
  "msg": "",
  "data": 5001
}
```

**`PUT /update`、`DELETE /delete`**：

```json
{
  "code": 0,
  "msg": "",
  "data": true
}
```

**`GET /get`** — `data` 为 `AppVersionRespVO`：

```json
{
  "code": 0,
  "msg": "",
  "data": {
    "id": 5001,
    "appId": 1001,
    "versionName": "1.2.0",
    "versionCode": 120,
    "apkUrl": "https://example.com/app.apk",
    "apkSize": 52428800,
    "updateDescription": "修复已知问题，优化性能",
    "status": 1,
    "createTime": "2024-06-10T12:00:00"
  }
}
```

**`GET /page`** — `data` 为 `PageResult<AppVersionRespVO>`：

```json
{
  "code": 0,
  "msg": "",
  "data": {
    "total": 1,
    "list": [
      {
        "id": 5001,
        "appId": 1001,
        "versionName": "1.2.0",
        "versionCode": 120,
        "apkUrl": "https://example.com/app.apk",
        "apkSize": 52428800,
        "updateDescription": "修复已知问题，优化性能",
        "status": 1,
        "createTime": "2024-06-10T12:00:00"
      }
    ]
  }
}
```

---

## 5. TV 端 - 应用

- **Tag**：`TV 端 - 应用`
- **Base Path**：`/appstore/app`
- **完整前缀**：`/app-api/appstore/app`

| 方法 | 路径 | 鉴权 | 说明 |
|------|------|------|------|
| GET | `/list` | `@PermitAll` | 获取已上架应用列表 |
| GET | `/get` | `@PermitAll` | 获取应用详情（含最新版本信息） |
| POST | `/download` | `@PermitAll` | 记录下载并返回 APK 下载地址 |

### 5.1 `GET /list`

Query 参数：`TvAppListReqVO`

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| `categoryId` | Long | 否 | 分类编号 |
| `keyword` | String | 否 | 搜索关键词（有值时走搜索） |

**`GET /list` 响应示例** — `data` 为 `AppRespVO` 数组（`screenshots` 为后端存储的 JSON 字符串）：

```json
{
  "code": 0,
  "msg": "",
  "data": [
    {
      "id": 1001,
      "categoryId": 10,
      "name": "芒果TV",
      "packageName": "com.mgtv.tv",
      "iconUrl": "https://example.com/icon.png",
      "description": "一款热门的视频应用",
      "screenshots": "[\"https://example.com/s1.png\"]",
      "developer": "芒果TV",
      "status": 1,
      "downloadCount": 10000,
      "latestVersionId": 5001,
      "createTime": "2024-06-01T10:00:00"
    }
  ]
}
```

无数据时 `data` 可为空数组：

```json
{
  "code": 0,
  "msg": "",
  "data": []
}
```

### 5.2 `GET /get`

Query 参数：

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| `id` | Long | 是 | 应用编号 |

**`GET /get` 响应示例** — `data` 为 `TvAppDetailRespVO`；应用不存在时 `data` 为 `null`。下列字段在业务上可能为空：`iconUrl`、`description`、`screenshots`（空数组）、`developer`、`categoryName`、以及最新版本相关字段（`versionName`、`versionCode`、`apkUrl`、`apkSize`、`updateDescription` 等，以实际序列化结果为准。

```json
{
  "code": 0,
  "msg": "",
  "data": {
    "id": 1001,
    "name": "芒果TV",
    "packageName": "com.mgtv.tv",
    "iconUrl": "https://example.com/icon.png",
    "description": "一款热门的视频应用",
    "screenshots": [
      "https://example.com/s1.png",
      "https://example.com/s2.png"
    ],
    "developer": "芒果TV",
    "downloadCount": 10001,
    "categoryName": "影音娱乐",
    "versionName": "1.2.0",
    "versionCode": 120,
    "apkUrl": "https://example.com/app.apk",
    "apkSize": 52428800,
    "updateDescription": "修复已知问题，优化性能"
  }
}
```

**应用不存在时：**

```json
{
  "code": 0,
  "msg": "",
  "data": null
}
```

### 5.3 `POST /download`

Query 参数：

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| `id` | Long | 是 | 应用编号 |

**`POST /download` 响应示例：**

```json
{
  "code": 0,
  "msg": "",
  "data": "https://example.com/app.apk"
}
```

无最新版本时 `data` 可为 `null`：

```json
{
  "code": 0,
  "msg": "",
  "data": null
}
```

---

## 6. TV 端 - 应用分类

- **Tag**：`TV 端 - 应用分类`
- **Base Path**：`/appstore/category`
- **完整前缀**：`/app-api/appstore/category`

| 方法 | 路径 | 鉴权 | 说明 |
|------|------|------|------|
| GET | `/list` | `@PermitAll` | 获取已启用分类列表 |

**`GET /list` 响应示例：**

```json
{
  "code": 0,
  "msg": "",
  "data": [
    {
      "id": 201,
      "name": "影音娱乐"
    },
    {
      "id": 202,
      "name": "游戏"
    }
  ]
}
```

---

## 7. TV 端 - 设备认证

- **Tag**：`TV 端 - 设备认证`
- **Base Path**：`/device`
- **完整前缀**：`/app-api/device`

| 方法 | 路径 | 鉴权 | 说明 |
|------|------|------|------|
| POST | `/register` | `@PermitAll` | 设备注册 |
| POST | `/token` | `@PermitAll` | 轮询设备授权状态 |
| POST | `/refresh` | `@PermitAll` | 刷新 Token |
| POST | `/logout` | `@PermitAll` | 登出 |

### 7.1 `POST /register`

请求体：`DeviceRegisterReqVO`

| JSON 字段 | 类型 | 必填 | 说明 |
|-----------|------|------|------|
| `device_id` | String | 是 | 设备唯一标识 |
| `device_model` | String | 是 | 设备型号 |
| `os_version` | String | 是 | 系统版本 |
| `app_version` | String | 是 | 应用版本号 |

**`POST /register` 响应示例**（字段名为 JSON 下划线风格）：

```json
{
  "code": 0,
  "msg": "",
  "data": {
    "device_code": "ABCD-EFGH",
    "activation_url": "https://example.com/activate?code=ABCD-EFGH",
    "expires_in": 600,
    "poll_interval": 3
  }
}
```

### 7.2 `POST /token`

请求体：`DeviceTokenReqVO`

| JSON 字段 | 类型 | 必填 | 说明 |
|-----------|------|------|------|
| `device_id` | String | 是 | 设备唯一标识 |
| `device_code` | String | 是 | 设备授权码 |

**`POST /token` 响应示例**（未授权完成时部分字段可能为空，以实际业务为准）：

```json
{
  "code": 0,
  "msg": "",
  "data": {
    "access_token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "refresh_token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "expires_in": 7200,
    "device_name": "客厅电视"
  }
}
```

### 7.3 `POST /refresh`

请求体：`RefreshTokenReqVO`

| JSON 字段 | 类型 | 必填 | 说明 |
|-----------|------|------|------|
| `refresh_token` | String | 是 | 刷新令牌 |

**`POST /refresh` 响应示例：**

```json
{
  "code": 0,
  "msg": "",
  "data": {
    "access_token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "refresh_token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "expires_in": 7200
  }
}
```

### 7.4 `POST /logout`

- 请求体：无
- 令牌获取：服务端会从 `Authorization` 或 `access_token` 中提取 access token。

**`POST /logout` 响应示例：**

```json
{
  "code": 0,
  "msg": "",
  "data": true
}
```

---

## 8. 请求体 JSON 示例（管理端与设备端）

### 8.1 创建 / 更新应用 `AppSaveReqVO`

```json
{
  "id": 1001,
  "categoryId": 10,
  "name": "芒果TV",
  "packageName": "com.mgtv.tv",
  "iconUrl": "https://example.com/icon.png",
  "description": "一款热门的视频应用",
  "screenshots": "[\"https://example.com/s1.png\"]",
  "developer": "芒果TV"
}
```

创建时可省略 `id`。

### 8.2 创建 / 更新分类 `CategorySaveReqVO`

```json
{
  "id": 201,
  "name": "影音娱乐",
  "iconUrl": "https://example.com/cat.png",
  "sort": 1,
  "status": 0
}
```

### 8.3 创建 / 更新应用版本 `AppVersionSaveReqVO`

```json
{
  "id": 5001,
  "appId": 1001,
  "versionName": "1.2.0",
  "versionCode": 120,
  "apkUrl": "https://example.com/app.apk",
  "apkSize": 52428800,
  "updateDescription": "修复已知问题",
  "status": 1
}
```

### 8.4 设备注册 `POST /app-api/device/register`

```json
{
  "device_id": "TV-ABC-001",
  "device_model": "MiTV-4A",
  "os_version": "Android 11",
  "app_version": "1.0.0"
}
```

### 8.5 设备轮询 Token `POST /app-api/device/token`

```json
{
  "device_id": "TV-ABC-001",
  "device_code": "ABCD-EFGH"
}
```

### 8.6 刷新 Token `POST /app-api/device/refresh`

```json
{
  "refresh_token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}
```

### 8.7 分页查询 Query 示例

- `GET /admin-api/appstore/app/page?pageNo=1&pageSize=10&name=芒果`
- `GET /admin-api/appstore/category/page?pageNo=1&pageSize=10&status=0`
- `GET /admin-api/appstore/app-version/page?appId=1001&pageNo=1&pageSize=10`
