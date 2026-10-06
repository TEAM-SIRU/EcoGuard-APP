# EcoGuard

환경지킴이 앱 레포지토리입니다.

## 프로젝트 상태

현재 프로젝트는 Kotlin과 Jetpack Compose를 사용하는 Android 앱입니다. 서버 연동·ViewModel·화면 전환 없이 화면별 정적 UI를 구현한 단계이며, 앱을 실행하면 홈 화면의 로딩 상태가 표시됩니다. 제품 기능, 대상 사용자, 전체 범위는 PRD가 없어 **확인 필요**입니다.

### 디자인 기준

- 디자인 원본: [환경지킴이 디자인 시스템의 ‘App Screens · Redesign’ 페이지 (238:2)](https://www.figma.com/design/Ef8bcly7z4kzYE4D7W9uXU/?node-id=238-2)
- 글꼴: Figma는 Noto Sans KR을 사용하지만, 팀 결정에 따라 앱은 Pretendard(`res/font`)를 유지합니다.
- 색상: Primary는 Figma 변수 값 `#57C144`를 사용합니다.
- 테마: 밝은 테마만 지원합니다. 다크 디자인이 확정되기 전까지 다크 색상 구성은 밝은 테마와 같습니다.
- 아이콘: Figma 원본을 `drawable` 리소스로 사용합니다.
- 반응형: 콘텐츠 최대 너비 600dp(`AppComponentSize.contentMaxWidth`)로 가운데 정렬하며, 320dp·840dp·`fontScale` 1.5 Preview로 확인합니다.

### 구현 범위

| 패키지 (`feature/*`) | 구현한 UI |
|---|---|
| `login` | 로그인, 교사 계정 안내 |
| `home` | 홈(로딩·불러오기 실패·활동 제외·콘텐츠 상태), 오늘/이번 주 청소, 모집·공지 카드 |
| `recruitment` | 모집 공고, 신청, 신청 결과, 불러오기 실패 |
| `area` | 청소 구역 |
| `verification` | 청소 인증 흐름(안내·촬영·사진 확인·제출 완료·업로드 실패·인증 시간 종료·인증 불가 시트), 인증 결과 |
| `appeal` | 이의신청 작성·제출 완료·내역·결과 |
| `activity` | 활동 기록, 월 선택 다이얼로그 |
| `menu` | 전체 메뉴 |
| `notice` | 공지 불러오기 실패 |

후속 작업(현재 범위 제외): Navigation 3 기반 화면 전환, 서버 연동, ViewModel 연결, CameraX를 이용한 실제 촬영. 구체적인 일정과 순서는 **확인 필요**입니다.

## 기술 스택

| 항목 | 설정 |
|---|---|
| 모듈 | `:app` |
| 언어 및 UI | Kotlin 2.2.10, Jetpack Compose, Material 3 |
| Android Gradle Plugin | 9.2.1 |
| Gradle Wrapper | 9.4.1 |
| Gradle daemon JDK | 21 |
| Java source/target | 11 |
| Application ID | `com.nativelap.ecoguard` |
| Android SDK | `compileSdk 37`, `minSdk 34`, `targetSdk 36` |

## 시작하기

### 요구사항

- JDK 21 (Gradle daemon toolchain)
- Android SDK 및 Android 빌드 도구

### 빌드

저장소를 받은 뒤 디버그 APK를 빌드합니다.

```bash
git clone https://github.com/TEAM-SIRU/EcoGuard-APP.git
cd EcoGuard-APP
./gradlew :app:assembleDebug
```

단위 테스트와 Android Lint는 다음 명령으로 실행할 수 있습니다.

```bash
./gradlew :app:testDebugUnitTest :app:lintDebug
```

오프라인 검증 명령은 다음과 같습니다. 현재 UI 변경의 실행 결과는 별도 완료 보고를 참고하세요.

```bash
./gradlew --offline :app:assembleDebug :app:testDebugUnitTest :app:lintDebug
```

GitHub Actions 설정은 [`.github/workflows/android-ci.yml`](.github/workflows/android-ci.yml)에 있습니다. 저장소 규칙상 통합·기본 브랜치는 `dev`, 릴리스 대상은 `main`입니다. Android 기반 작업은 [Issue #1](https://github.com/TEAM-SIRU/EcoGuard-APP/issues/1)에서 `🔧 chore` 라벨로 관리하며, 해당 PR #2는 아직 `dev`에 병합되지 않았습니다. 디자인 시스템 기반 화면 UI 작업은 [Issue #3](https://github.com/TEAM-SIRU/EcoGuard-APP/issues/3)에서 진행합니다.

## 프로젝트 구조

```text
EcoGuard-APP/
├── app/                         # Android 앱 모듈
│   └── src/
│       ├── main/                 # 앱 진입점, 화면별 UI(feature), 공통 컴포넌트·테마, 리소스
│       ├── test/                 # 로컬 단위 테스트
│       └── androidTest/          # 기기 계측 테스트
├── .github/                      # Issue/PR 템플릿 및 Actions workflow
├── gradle/libs.versions.toml     # 플러그인 및 라이브러리 버전
├── build.gradle.kts              # 루트 Gradle 플러그인 설정
└── settings.gradle.kts           # 프로젝트 및 모듈 설정
```

앱 실행 흐름은 `MainActivity` → `HomeRoute`이며, `HomeRoute`는 기본값인 `HomeUiState.Loading` 상태로 `HomeLoadingScreen`을 표시합니다. 다른 화면은 아직 진입 경로가 없고 Preview로 확인합니다.

`app/src/main/java/com/nativelap/ecoguard/` 아래 구성은 다음과 같습니다.

- `feature/<기능>/view/`: Route, Screen, 화면 전용 Composable, Preview용 `*PreviewFixtures`(일부 기능만)
- `feature/<기능>/viewmodel/`: `*UiState`, `*ScreenEvent`, `*UiModel` 등 화면 상태 타입(`notice`는 없음). 이름과 달리 ViewModel 클래스는 아직 없습니다.
- `ui/component/`: 여러 화면이 공유하는 컴포넌트(버튼, 상단 바, 하단 탭, 다이얼로그, 바텀시트, 스켈레톤 등)
- `ui/theme/`: `Theme.kt`, `AppSpacing`, `AppRadius`, `AppComponentSize`, `AppIconSize`, `AppTypography`·`AppExtraTypography`, `AppExtraColors`

Route는 전달받은 고정 상태(기본값 또는 Preview 주입 값)만 표시하며, ViewModel이나 서버 데이터 흐름은 없습니다.

## 문서와 확인 항목

- PRD는 아직 없습니다. 앱의 목표, 대상 사용자, 기능 범위는 **확인 필요**입니다.
