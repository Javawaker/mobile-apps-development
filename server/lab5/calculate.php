<?php
header('Content-Type: application/json; charset=utf-8');

// Принимаем параметры из запроса
$liquidName = $_GET['liquidName'];
$volume = $_GET['volume'];
$heaterWattage = $_GET['heaterWattage'];

// Определяем свойства жидкостей
$liquids = [
    "вода" => ["heatCapacity" => 4186, "boilingTemperature" => 100, "density" => 1],
    "этиловый спирт" => ["heatCapacity" => 2470, "boilingTemperature" => 78.37, "density" => 0.789],
    "нефть" => ["heatCapacity" => 2100, "boilingTemperature" => 140, "density" => 0.850],
    "глицерин" => ["heatCapacity" => 2430, "boilingTemperature" => 290, "density" => 1.252],
    "бензол" => ["heatCapacity" => 1050, "boilingTemperature" => 80.1, "density" => 0.876]
];

if($volume == 0 && $heaterWattage == 0) {
    echo json_encode(["error" => "Введите значения больше нуля"],JSON_UNESCAPED_UNICODE);
}
else if($volume == 0) {
    echo json_encode(["error" => "Значение объема должно быть больше нуля"],JSON_UNESCAPED_UNICODE);
}
else if($heaterWattage == 0) {
    echo json_encode(["error" => "Значение мощности должно быть больше нуля"],JSON_UNESCAPED_UNICODE);
}
else {
    // Выбираем жидкость
    if(array_key_exists($liquidName, $liquids)) {
        $liquid = $liquids[$liquidName];
        $startTemp = 24; // Начальная температура в °C

        // Вычисляем время до кипения
        $timeToBoil = ($liquid["heatCapacity"] * $volume * $liquid["density"] * ($liquid["boilingTemperature"] - $startTemp)) / ($heaterWattage); // В секундах

        // Формируем и отправляем ответ
        $response = [
            "error" => false,
            "timeToBoil" => round($timeToBoil, 2),
            "heatCapacity" => $liquid["heatCapacity"],
            "boilingTemperature" => $liquid["boilingTemperature"]
        ];
        echo json_encode($response,JSON_UNESCAPED_UNICODE);
    } else {
        // Жидкость не найдена
        echo json_encode(["error" => "Жидкость не найдена"],JSON_UNESCAPED_UNICODE);
    }
}
?>
