# 러닝 트래커 앱 기술 검토 및 설계 (학습용)

이 문서는 사용자의 기획안을 바탕으로 안드로이드 기술 스택 및 클린 아키텍처 관점에서 세부 설계 내용을 정리한 자료입니다.

## 1. 핵심 기능 및 기술적 고려사항

### 1.1 시작 및 종료 (포그라운드 서비스)
- **운동 시작**: `Context.startForegroundService()`를 호출하여 사용자가 앱을 닫아도 위치 추적이 멈추지 않도록 합니다.
- **알림(Notification)**: 운동 상태를 실시간으로 노출하며, '일시정지/종료' 버튼을 알림창에 직접 배치하여 사용자 경험을 향상시킵니다.
- **START_STICKY**: 시스템에 의해 서비스가 강제 종료되더라도 다시 살아나도록 설정합니다.

### 1.2 실시간 경로 추적 및 지도 (UI/UX)
- **FusedLocationProviderClient**: 배터리 효율을 고려하면서 정확한 위치 정보를 수집합니다.
- **Polyline**: 수집된 좌표(`LatLng`) 리스트를 구글 지도 위에 실시간으로 렌더링합니다.
- **상태 관리**: ViewModel에서 좌표 흐름을 `StateFlow`로 관리하여 지도가 부드럽게 업데이트되도록 합니다.

### 1.3 데이터 저장 및 복구
- **Room DB**: 운동이 종료된 최종 데이터와 경로 정보(`JSON` 타입 등)를 로컬 DB에 안정적으로 저장합니다.
- **강제 종료 대응**: 실시간으로 수집되는 좌표를 주기적으로 임시 저장하여 앱이 비정상 종료되더라도 데이터를 최대한 보존하는 전략을 취합니다.

## 2. 권한 및 보안 설정

### 2.1 위치 권한 (API 레벨별 대응)
- `ACCESS_FINE_LOCATION`, `ACCESS_COARSE_LOCATION` 필수 요청.
- Android 10 이상: `ACCESS_BACKGROUND_LOCATION` 추가 요청 필요.
- Android 14 이상: `FOREGROUND_SERVICE_LOCATION` 타입 선언 필수.

### 2.2 배터리 최적화
- 시스템이 위치 수집 서비스를 잠재우지 않도록 `REQUEST_IGNORE_BATTERY_OPTIMIZATIONS` 처리를 고려합니다.

## 3. 권장 프로젝트 구조 (Clean Architecture)

```
com.ezlevup.runningtracker
├── data
│   ├── local        # Room DB (Entities, DAOs)
│   ├── repository   # Repository 구현 (Data Source 제어)
│   └── service      # LocationService (실제 위치 수집 로직)
├── domain
│   ├── model        # 비즈니스 데이터 모델 (Run, PathPoint)
│   ├── repository   # Repository 인터페이스 정의
│   └── usecase      # 비즈니스 로직 단위 (운동 기록 저장 등)
└── presentation
    ├── home         # 메인 운동 화면 (지도, 컨트롤)
    ├── history      # 과거 기록 조회 화면
    └── components   # 재사용 가능한 UI 컴포넌트
```

> [!TIP]
> **공부용 포인트**: 각 레이어 간의 의존성을 한 방향(Domain 레이어 쪽으로)으로 흐르게 설계하여 유지보수성과 테스트 용이성을 확보합니다.
