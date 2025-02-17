<?php
header('Content-Type: application/json');

// Принимаем параметры из запроса
$liquidName = $_GET['liquid'];
$volume = $_GET['volume'];
$heaterWattage = $_GET['heaterWattage'];

// Определяем свойства жидкостей
$liquids = [
    "вода" => ["heatCapacity" => 4186, "boilingTemperature" => 100, "density" => 1],
    "этил" => ["heatCapacity" => 2470, "boilingTemperature" => 78.37, "density" => 0.789],
    "нефть" => ["heatCapacity" => 2100, "boilingTemperature" => 140, "density" => 0.850],
    "глицерин" => ["heatCapacity" => 2430, "boilingTemperature" => 290, "density" => 1.252],
    "бензин" => ["heatCapacity" => 1050, "boilingTemperature" => 80.1, "density" => 0.876]
];

// Выбираем жидкость
if(array_key_exists($liquidName, $liquids)) {
    $liquid = $liquids[$liquidName];
    $startTemp = 24; // Начальная температура в °C

    // Вычисляем время до кипения
    $timeToBoil = ($liquid["heatCapacity"] * $volume * $liquid["density"] * ($liquid["boilingTemperature"] - $startTemp)) / ($heaterWattage); // В секундах

    // Формируем и отправляем ответ
    $response = [
        "timeToBoil" => $timeToBoil,
        "heatCapacity" => $liquid["heatCapacity"],
        "boilingTemperature" => $liquid["boilingTemperature"]
    ];
    echo json_encode($response);
} else {
    // Жидкость не найдена
    http_response_code(404);
    echo json_encode(["error" => "Liquid not found"]);
}
?>
