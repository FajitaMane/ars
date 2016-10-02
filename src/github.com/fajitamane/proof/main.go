package main

import(
	"fmt"
	"math"
	_ "math/rand"
)

const MAX_NODES = 4

type Point struct {
	X float64 //Latitude
	Y float64	//Longitude
}

type Map struct {
	Points [4]Point
}

func NewMap(p1, p2, p3, p4 Point) Map {
	var points [4]Point
	points[0] = p1
	points[1] = p2
	points[2] = p3
	points[3] = p4
	return Map{points}
}

func (p1 *Point) distTo(p2 *Point) float64 {
	v := math.Sqrt(math.Pow(p2.X - p1.X, 2) + math.Pow(p2.Y - p1.Y, 2))
	fmt.Println(v)
	return v
}

func (p *Point) ToString() string {
	return "(" + string(int(p.X)) + ", " + string(int(p.Y)) + ")"
}

func (m *Map) Perimeter() float64 {
	var val float64
	for i := 0; i < len(m.Points); i++ {
		val += m.Points[i].distTo(&m.Points[(i + 1) % (len(m.Points))])
	}
	return val
}

func main() {
	p1 := Point{0.000, 0.000}
	p2 := Point{0.000, 5.000}
	p3 := Point{5.000, 5.000}
	p4 := Point{5.000, 0.000}
	var m Map
	m = NewMap(p1, p2, p3, p4)
	//fmt.Println(p1.ToString())
	//fmt.Println(p2.ToString())
	//fmt.Println(p1.distTo(&p2))
	fmt.Println(m.Perimeter())
}