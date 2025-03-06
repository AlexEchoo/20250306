# Controller Jitter Project

## 项目概述
控制器抖动控制项目，用于[在此描述项目的主要目标和功能]

## 项目结构
```
.
├── route/                          # 主项目目录
│   ├── data/                      # 数据文件目录
│   │   ├── 108.txt
│   │   ├── 1080.txt
│   │   ├── 108fan.txt
│   │   ├── 120.txt
│   │   ├── 1515sat.txt
│   │   └── [其他数据文件]
│   │
│   └── route-provider/            # 核心服务提供模块
│       ├── src/
│       │   ├── main/
│       │   │   ├── java/
│       │   │   │   └── com/
│       │   │   │       └── pml/
│       │   │   │           ├── route/
│       │   │   │           │   └── business/
│       │   │   │           │       ├── JitterControl/    # 抖动控制实现
│       │   │   │           │       ├── label/           # 标签相关实现
│       │   │   │           │       ├── path/            # 路径相关实现
│       │   │   │           │       ├── sr/              # SR相关实现
│       │   │   │           │       └── topo/            # 拓扑相关实现
│       │   │   │           ├── sdsn/                    # SDSN通用模块
│       │   │   │           └── util/                    # 工具类
│       │   │   └── resources/                           # 配置文件目录
│       │   └── META-INF/
│       └── pom.xml                                      # Maven配置文件
```

## 主要组件说明

### 1. JitterControl
- 位置：`route/route-provider/src/main/java/com/pml/route/business/JitterControl/`
- 功能：实现抖动控制的核心逻辑

### 2. 业务模块 (Business)
- Label处理：`route/route-provider/src/main/java/com/pml/route/business/label/`
- 路径处理：`route/route-provider/src/main/java/com/pml/route/business/path/`
- SR实现：`route/route-provider/src/main/java/com/pml/route/business/sr/`
- 拓扑管理：`route/route-provider/src/main/java/com/pml/route/business/topo/`

### 3. 数据文件
- 位置：`route/data/`
- 用途：存储系统所需的各种数据文件
- 主要文件：
  - 108.txt
  - 1080.txt
  - 108fan.txt
  - 120.txt
  - 1515sat.txt
  等

## 配置文件
- 主配置：`route/route-provider/src/main/resources/application.yml`
- 日志配置：`route/route-provider/src/main/resources/logback.xml`

## 如何运行

### 环境要求
- JDK版本：[指定版本]
- Maven版本：[指定版本]
- 其他依赖：[列出其他必要的依赖]

### 构建步骤
1. 克隆仓库
```bash
git clone https://github.com/AlexEchoo/20250306.git
```

2. 进入项目目录
```bash
cd 20250306/route/route-provider
```

3. 构建项目
```bash
mvn clean install
```

### 运行应用
[添加具体的运行说明和命令]

## 开发指南
[添加开发相关的说明和指导]

## 维护者
- [@AlexEchoo](https://github.com/AlexEchoo)

## 更新日志
- 2025-03-06: 初始版本上传

## 许可证
[添加许可证信息]
