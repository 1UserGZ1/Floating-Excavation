#!/bin/bash
# 多版本构建脚本

echo "开始构建浮空挖掘模组..."

# 版本列表
VERSIONS=("1.20.1" "1.20.2" "1.20.3" "1.20.4" "1.20.5" "1.20.6")

# Forge版本映射（根据需要调整）
declare -A FORGE_VERSIONS=(
    ["1.20.1"]="47.4.0"
    ["1.20.2"]="48.0.40"
    ["1.20.3"]="49.0.0"
    ["1.20.4"]="49.0.0"
    ["1.20.5"]="50.0.0"
    ["1.20.6"]="51.0.0"
)

# 清理旧的构建文件
echo "清理旧构建..."
./gradlew clean

# 为每个版本构建
for VERSION in "${VERSIONS[@]}"; do
    echo ""
    echo "========================================"
    echo "构建 Minecraft ${VERSION} 版本"
    echo "========================================"

    FORGE_VERSION=${FORGE_VERSIONS[$VERSION]}

    # 执行构建
    ./gradlew build -PmcVersion=$VERSION -PforgeVersion=$FORGE_VERSION -x test

    # 检查构建是否成功
    if [ $? -eq 0 ]; then
        echo "✓ Minecraft ${VERSION} 构建成功"
    else
        echo "✗ Minecraft ${VERSION} 构建失败"
    fi
done

echo ""
echo "所有版本构建完成！"
echo "构建文件保存在: build/libs/"