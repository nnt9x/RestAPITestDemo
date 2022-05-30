Feature: test book API

  Scenario: tao tai khoan nguoi dung moi
  Given cho userName hop le "demoUser342"
  And cho password hop le "Bkacad@123"
  When tao tai khoan nguoi dung
  Then lay ra userID

  Scenario: lay token voi nguoi dung da xac nhan
    Given cho userName hop le "demoUser342"
    And cho password hop le "Bkacad@123"
    When thuc hien lay token
    Then in ra token


  Scenario: tai khoan da xac thuc co the them, xoa sach khoi reading list
    Given tai khoan da xac thuc
    When them mot cuon sach ma ISBN "9781449337711"
    Then cuon sach duoc them vao tai khoan






