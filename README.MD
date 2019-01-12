# 传奇霸业GM工具

### 关于GM命令
> 在游戏聊天输入框中可以执行GM命令，目前已知两个GM命令

1. 制造物品
```
!make [物品ID]
```
2. 提升等级
```
!level [提升的等级数]
```

### 关于角色背包
> 穿戴物品slot值如下表。背包空间为64，slot值从23开始到86结束。

##### 穿戴物品信息表

| 物品类型 | slot值 |
| :------: | :------: |
| 头盔 | 0 |
| 项链 | 1 |
| 手镯（左）| 2 |
| 手镯（右）| 3 |
| 戒指（左）| 4 |
| 戒指（右）| 5 |
| 腰带 | 6 |
| 靴子 | 7 |
| 武器 | 8 |
| 衣服 | 9 |
| 勋章 | 10 |
| 护盾 | 11 |
| 宝石 | 12 |
| 血符 | 13 |
| 魂珠 | 14 |
| 面甲 | 15 |
| 吊坠 | 16 |
| 护肩 | 17 |
| 护膝 | 18 |
| 时装 | 19 |
| 羽翼 | 21 |

### 关于装备强化
> 装备最大强化等级为12级，只有血符不能强化。强化字段为【strengthlevel】，强化1级该值为65537，以此类推，满级（12）的值为786444

### 关于装备注灵
> 装备最大注灵等级为12级，只有血符不能注灵。注灵字段为【btaoattack】，数字为几代表注灵几级，满级（12）的值为12

### 关于物品
> 物品种类通过class和subclass两个字段标识，[*]代表任意值，allowableclass为物品使用的职业：0-任意职业；1-战士；2-法师；3-道士，allowablerace为物品使用的性别

| 物品类别 | class | subclass | inventorytype |
| :------: | :------: | :------: | :------: |
| 武器 | 2 | * | * |
| 项链 | 3 | 2 | 2 |
| 吊坠 | 3 | 2 | 14 |
| 宝石 | 3 | 3 | * |
| 勋章 | 3 | 5 | * |
| 血符 | 3 | 7 | * |
| 头盔 | 4 | 1 | 1 |
| 面甲 | 4 | 1 | 10 |
| 衣服 | 4 | 4 | 4 |
| 时装 | 4 | 4 | 22 |
| 腰带 | 4 | 6 | 6 |
| 靴子 | 4 | 6 | 8 |
| 护肩 | 4 | 6 | 15 |
| 魂珠 | 4 | 6 | 16 |
| 护膝 | 4 | 6 | 19 |
| 护盾 | 4 | 9 | * |
| 戒指 | 4 | 11 | * |
| 手镯 | 4 | 12 | * |
| 羽翼 | 4 | 29 | * |

### 打包命令
> Windows exe
```
cd gm-tools
mvn clean package
cd target
javapackager -deploy -native image -outdir build -srcdir dist -appclass bing.cqby.Main -name gm-tools -outfile gm-tools

# 不将第三方类库打入同一个jar中
javapackager -deploy -native image -outdir final -srcdir release -appclass bing.cqby.Main -name gm-tools -outfile gm-tools
```