class BicyclesController < ApplicationController

  def index
    @bicycles = Bicycle.all
  end

  def show
    @bicycle = Bicycle.find(params[:id])
  end

  def new
    @bicycle = Bicycle.new
  end

  def edit
    @bicycle = Bicycle.find(params[:id])
  end

  def create
    @bicycle = Bicycle.new(bicycle_params)

    if@bicycle.save
      redirect_to @bicycle
    else
      render 'new'
    end
  end

  def update
    @bicycle = Bicycle.find(params[:id])

    if @bicycle.update(bicycle_params)
      redirect_to @bicycle
    else
      render 'edit'
    end
  end

  def destroy
    @bicycle = Bicycle.find(params[:id])
    @bicycle.destroy

    redirect_to bicycles_path
  end

  private
    def bicycle_params
      params.require(:bicycle).permit(:biketype, :brand, :model, :serial, :notes)
    end

end
