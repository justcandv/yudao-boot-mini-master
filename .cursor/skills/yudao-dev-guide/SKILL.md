---
name: yudao-dev-guide
description: >-
  芋道 yudao-boot-mini (JDK17) 项目的二次开发规范与约束。当用户要求新增模块、编写
  Controller/Service/Mapper/DO/VO、定义错误码、配置权限、或对该项目进行任何代码修改时，
  必须遵循此技能中的规则。Use when developing new features, adding modules, writing
  CRUD code, or making any code changes in this yudao project.
---

# 芋道 yudao-boot-mini 二次开发规范

## 技术栈概要

| 项目 | 版本/选型 |
|------|-----------|
| JDK | 17 |
| Spring Boot | 3.x（`jakarta.*` 命名空间） |
| 构建工具 | Maven 多模块 |
| ORM | MyBatis-Plus + MyBatis Plus Join |
| 数据库 | 多数据库（MySQL/PostgreSQL/Oracle 等） |
| 安全 | Spring Security 6 + JWT（无 Session） |
| 接口文档 | Springdoc OpenAPI 3 + Knife4j |
| 工具库 | Lombok、MapStruct、Hutool |
| 代码风格 | 阿里巴巴 Java 开发手册 |

---

## 1. 模块结构

### 1.1 新增业务模块

新业务模块必须以 `yudao-module-{模块名}` 命名，放在根目录下，并在根 `pom.xml` 的 `<modules>` 中注册。

```
yudao-module-{模块名}/
├── pom.xml
└── src/main/java/cn/iocoder/yudao/module/{模块名}/
    ├── package-info.java          # 模块说明（URL 前缀、表名前缀）
    ├── controller/
    │   ├── admin/                  # 管理端 API（自动加 /admin-api 前缀）
    │   │   └── {功能}/
    │   │       ├── {功能}Controller.java
    │   │       └── vo/             # 请求/响应 VO
    │   └── app/                    # 用户端 API（自动加 /app-api 前缀）
    ├── service/
    │   └── {功能}/
    │       ├── {功能}Service.java       # 接口
    │       └── {功能}ServiceImpl.java   # 实现
    ├── dal/
    │   ├── dataobject/             # DO 实体
    │   │   └── {功能}/
    │   │       └── {Xxx}DO.java
    │   └── mysql/                  # Mapper 接口
    │       └── {功能}/
    │           └── {Xxx}Mapper.java
    ├── convert/                    # MapStruct 转换器（如需要）
    ├── enums/
    │   └── ErrorCodeConstants.java # 本模块错误码
    ├── job/                        # 定时任务
    └── framework/                  # 模块级框架扩展
```

### 1.2 package-info.java 约定

每个模块必须创建 `package-info.java`，声明：
- Controller URL 以 `/{模块名}/` 开头
- 数据表以 `{模块名}_` 为前缀

### 1.3 模块依赖规则

- **yudao-framework** 中的 starter 按需引入，不可引入不需要的 starter
- 业务模块之间的依赖应单向，避免循环：`infra` 是底层，`system` 依赖 `infra`
- 新模块需在 `yudao-server/pom.xml` 中添加依赖才能生效

---

## 2. 分层编码规范

### 2.1 Controller 层

```java
@Tag(name = "管理后台 - {功能名}")
@RestController
@RequestMapping("/{模块名}/{功能名}")
@Validated
public class {Xxx}Controller {

    @Resource
    private {Xxx}Service {xxx}Service;

    @PostMapping("/create")
    @Operation(summary = "创建{功能名}")
    @PreAuthorize("@ss.hasPermission('{模块名}:{功能名}:create')")
    public CommonResult<Long> create{Xxx}(@Valid @RequestBody {Xxx}SaveReqVO createReqVO) {
        return success({xxx}Service.create{Xxx}(createReqVO));
    }
}
```

**关键规则**：
- 统一返回 `CommonResult<T>`，使用静态导入 `CommonResult.success`
- 使用 `@Tag` + `@Operation` 注解 OpenAPI 文档
- 权限格式：`@PreAuthorize("@ss.hasPermission('{模块}:{功能}:{操作}')")`
- 精简列表接口使用 `@PermitAll` 或不加权限注解
- DO → VO 转换使用 `BeanUtils.toBean()`
- 使用 `jakarta.*` 而非 `javax.*`

### 2.2 Service 层

- 定义**接口** `{Xxx}Service` + **实现** `{Xxx}ServiceImpl`
- 实现类加 `@Service` + `@Validated`
- 使用 `@Resource` 注入 Mapper（非 `@Autowired`）
- 业务异常统一抛出 `ServiceException`，通过 `ErrorCodeConstants` 中的错误码构造
- 参数直接使用 ReqVO，DO 转换用 `BeanUtils.toBean()`

### 2.3 DAL 层（数据访问）

**DO 实体**：
- 普通表继承 `BaseDO`（含 createTime/updateTime/creator/updater/deleted）
- 多租户表继承 `TenantBaseDO`
- 类名后缀 `DO`，`@TableName("{模块名}_{表名}")`
- 使用 `@TableId` 标注主键，`@KeySequence` 支持非 MySQL 数据库

**Mapper 接口**：
- 继承 `BaseMapperX<{Xxx}DO>`，加 `@Mapper` 注解
- 查询方法写在 Mapper 中作为 `default` 方法
- 使用 `LambdaQueryWrapperX` + `likeIfPresent`/`eqIfPresent` 等条件拼接
- 分页查询返回 `PageResult<T>`

### 2.4 VO 规范

| 类型 | 命名 | 用途 |
|------|------|------|
| `{Xxx}SaveReqVO` | 创建 + 修改合用 | `id` 字段可选，有则更新无则创建 |
| `{Xxx}PageReqVO` | 分页查询请求 | 继承 `PageParam` |
| `{Xxx}ListReqVO` | 列表查询请求 | 不分页场景 |
| `{Xxx}RespVO` | 详情响应 | 完整字段 |
| `{Xxx}SimpleRespVO` | 精简响应 | 下拉选项等精简场景 |

- VO 放在 `controller/.../vo/` 目录下
- 使用 `@Schema` 注解描述字段（OpenAPI 文档）
- 校验注解：`@NotBlank` / `@NotNull` / `@Size` / `@Email` / `@InEnum` 等

---

## 3. 错误码规范

每个模块定义 `ErrorCodeConstants` 接口，错误码格式为 `1-{模块段}-{功能段}-{序号}`：

| 模块 | 错误码段 |
|------|----------|
| system | `1-002-xxx-xxx` |
| infra | `1-001-xxx-xxx` |
| 新模块 | 选取未占用段，如 `1-003-xxx-xxx` |

```java
public interface ErrorCodeConstants {
    // ========== {功能} 模块 1-0xx-001-000 ==========
    ErrorCode XXX_NOT_EXISTS = new ErrorCode(1_0xx_001_000, "{功能}不存在");
}
```

---

## 4. 权限与安全

- 权限标识格式：`{模块}:{功能}:{操作}`（如 `system:dept:create`）
- 管理端接口通过 `@PreAuthorize("@ss.hasPermission('...')")` 控制
- 公开接口使用 `@PermitAll`，或配置在 `yudao.security.permit-all-urls`
- 密码加密使用 `BCryptPasswordEncoder`
- 用户端接口放在 `controller/app/` 包下

---

## 5. API 设计约定

| 操作 | HTTP 方法 | 路径 | 返回值 |
|------|-----------|------|--------|
| 创建 | POST | `/create` | `CommonResult<Long>`（ID） |
| 更新 | PUT | `/update` | `CommonResult<Boolean>` |
| 删除 | DELETE | `/delete` | `CommonResult<Boolean>` |
| 批量删除 | DELETE | `/delete-list` | `CommonResult<Boolean>` |
| 单个查询 | GET | `/get` | `CommonResult<{Xxx}RespVO>` |
| 列表查询 | GET | `/list` | `CommonResult<List<{Xxx}RespVO>>` |
| 分页查询 | GET | `/page` | `CommonResult<PageResult<{Xxx}RespVO>>` |

- URL 路径前缀自动添加：admin 端为 `/admin-api`，app 端为 `/app-api`
- 参数使用 `@RequestParam` 或 `@RequestBody`，删除用 `@RequestParam("id")`

---

## 6. 数据库规范

- 表名：`{模块名}_{功能名}`（如 `system_dept`）
- 每张表必须包含 `BaseDO` 审计字段：`create_time`、`update_time`、`creator`、`updater`、`deleted`
- 逻辑删除字段 `deleted`，由 `@TableLogic` 自动处理
- 主键使用自增 `id` + `@KeySequence` 兼容多数据库
- SQL 初始化脚本放 `sql/{数据库类型}/` 目录

---

## 7. 配置文件

- 主配置文件：`yudao-server/src/main/resources/application.yaml`
- 环境配置：`application-local.yaml` / `application-dev.yaml`
- 新模块的业务配置统一放在 `yudao:` 命名空间下
- **严禁**提交真实密钥、API Key、数据库密码等敏感信息

---

## 8. 编码风格

- 遵循**阿里巴巴 Java 开发手册**
- 广泛使用 Lombok：`@Data`、`@Slf4j`、`@RequiredArgsConstructor` 等
- 依赖注入优先用 `@Resource`
- 对象转换使用 `BeanUtils.toBean()` / `BeanUtils.toBean(list, XxxRespVO.class)`
- 使用 `jakarta.*` 包（Spring Boot 3），而非 `javax.*`
- 类注释包含中文功能说明和 `@author`

---

## 9. 代码生成

项目内置代码生成器（`yudao-module-infra`），可通过管理后台生成 CRUD 代码：
- 配置在 `application.yaml` 的 `yudao.codegen` 段
- 生成代码后需检查并调整：确认分层正确、VO 字段合理、权限标识一致

---

## 10. 新增功能 Checklist

开发新功能时，按顺序完成：

- [ ] 建数据表（含审计字段），编写 SQL 脚本
- [ ] 创建 DO 实体（继承 `BaseDO` 或 `TenantBaseDO`）
- [ ] 创建 Mapper 接口（继承 `BaseMapperX`）
- [ ] 定义错误码（`ErrorCodeConstants`）
- [ ] 创建 Service 接口 + 实现
- [ ] 创建 VO（SaveReqVO / PageReqVO / RespVO）
- [ ] 创建 Controller（注解完整：OpenAPI + 权限 + 校验）
- [ ] 菜单与权限数据初始化
- [ ] 接口测试与文档验证

## 详细示例

完整的 CRUD 代码示例请参考 [examples.md](examples.md)。
